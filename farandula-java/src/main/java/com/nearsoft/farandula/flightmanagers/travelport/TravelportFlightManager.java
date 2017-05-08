package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.flightmanagers.travelport.request.xml.TravelportXMLRequest;
import com.nearsoft.farandula.models.*;
import com.nearsoft.farandula.utilities.XmlUtils;
import net.minidev.json.JSONArray;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.w3c.dom.*;
import org.w3c.dom.Node;


import javax.xml.soap.*;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 4/20/17.
 */
public class TravelportFlightManager implements FlightManager {

    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();

    private static String apiKey;

    private static String apiPassword;

    private static String targetBranch;

    static {
        Properties props = new Properties();
        try {
            props.load(TravelportFlightManager.class.getResourceAsStream("/config.properties"));
            apiKey = props.getProperty("travelport.apiUser");
            apiPassword = props.getProperty("travelport.apiPassword");
            targetBranch = props.getProperty("travelport.targetBranch");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AirLeg> getAvail(SearchCommand search) throws FarandulaException {

        try {
            SOAPMessage request = buildRequestForAvail(search);
            List<AirLeg> flightList = parseAvailResponse(request, search);
            return flightList;

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    public SOAPMessage buildRequestForAvail(SearchCommand search) throws SOAPException, IOException {

        //create SOAP envelope
        String envelope = buildEnvelopeStringFromSearch(search);

        // Send SOAP Message to SOAP Server
        SOAPMessage message = buildSOAPMessage(envelope);

        String url_api = "https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService";
        SOAPMessage soapResponse = sendRequest(message, url_api);

        return soapResponse;
    }

    public String buildEnvelopeStringFromSearch(SearchCommand search) {

        return TravelportXMLRequest.getRequest( search, targetBranch );

    }

    private SOAPMessage buildSOAPMessage(String envelope) throws SOAPException, IOException {

        MessageFactory factory = MessageFactory.newInstance();
        //adds envelope string to soapMessage
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(envelope.getBytes(Charset.forName("UTF-8"))));
        return message;

    }

    public SOAPMessage sendRequest(SOAPMessage message, String url_api) throws SOAPException {

        MimeHeaders headers = message.getMimeHeaders();
        headers.addHeader("Authorization", "Basic " + getAuthEncoded());
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        return soapConnection.call(message, url_api);
    }

    public List<AirLeg> parseAvailResponse(SOAPMessage response, SearchCommand searchCommand) throws IOException, SOAPException, ParseException {

        List<AirLeg> airLegs = new ArrayList<>();
        SOAPEnvelope env = response.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();

        //for connections
        NodeList listItinerary = body.getElementsByTagName("air:Connection");
        List<Integer> connectionList = new ArrayList<>();
        for (int i = 0; i < listItinerary.getLength(); i++) {
            Node currentNode = listItinerary.item(i);
            NamedNodeMap nodeAttributes = currentNode.getAttributes();
            connectionList.add(Integer.valueOf(nodeAttributes.getNamedItem("SegmentIndex").getNodeValue().toString()));
        }

        //flights
        NodeList listFlights = body.getElementsByTagName("air:FlightDetails");
        List<FlightDetailsTravelport> resultFlightsDetails = new LinkedList<>();
        for (int i = 0; i < listFlights.getLength(); i++) {
            Node currentNode = listFlights.item(i);
            NamedNodeMap nodeAttributes = currentNode.getAttributes();
            resultFlightsDetails.add(getFlightDetails(nodeAttributes));
        }

        //segments
        NodeList list = body.getElementsByTagName("air:AirSegment");
        List<Segment> connectedSegments = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {

            Node airSegmentNode = list.item(i);
            NamedNodeMap nodeAttributes = airSegmentNode.getAttributes();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            Segment seg = new Segment();
            seg.setAirlineIconPath("");
            seg.setMarketingAirline(nodeAttributes.getNamedItem("Carrier").getNodeValue().toString());
            seg.setMarketingFlightNumber(nodeAttributes.getNamedItem("FlightNumber").getNodeValue().toString());

            parseCodeshareChild(seg, airSegmentNode);
            parseAirAvailInfoChild(seg, airSegmentNode);


            //TODO travel class for travelport
            seg.setTravelClass("");
            seg.setAirplaneData(resultFlightsDetails.get(i).getEquipment());

            //departure data
            seg.setDepartureAirportCode(nodeAttributes.getNamedItem("Origin").getNodeValue().toString());
            seg.setDepartureTerminal(resultFlightsDetails.get(i).getOriginalTerminal());
            LocalDateTime departingDateTime = LocalDateTime.parse(
                    nodeAttributes.getNamedItem("DepartureTime").getNodeValue().toString(), formatter);
            seg.setArrivalDate(departingDateTime);

            //arrival data
            seg.setArrivalAirportCode(nodeAttributes.getNamedItem("Destination").getNodeValue().toString());
            seg.setArrivalTerminal(resultFlightsDetails.get(i).getDestinationTerminal());
            LocalDateTime arrivalDateTime = LocalDateTime.parse(
                    nodeAttributes.getNamedItem("ArrivalTime").getNodeValue().toString(), formatter);
            seg.setArrivalDate(arrivalDateTime);

            seg.setDuration(resultFlightsDetails.get(i).getFlightTime());

            connectedSegments.add( seg );

            //case when the last segment arrival is the search arrival airport OR
            //case for the returning airleg, i.e case when the arrival segment is the departure airport in search
            if ( seg.getArrivalAirportCode().equals( searchCommand.getArrivalAirport() )  ||
                    seg.getArrivalAirportCode().equals( searchCommand.getDepartureAirport() )
                    ){

                AirLeg leg = new AirLeg();
                leg.setId("tempID");
                leg.setDepartureAirportCode(connectedSegments.get(0).getDepartureAirportCode());
                leg.setDepartingDate(connectedSegments.get(0).getDepartingDate());
                leg.setArrivalAirportCode(connectedSegments.get( connectedSegments.size() - 1).getArrivalAirportCode());
                leg.setArrivalDate(connectedSegments.get(connectedSegments.size() - 1).getArrivalDate());
                leg.setSegments(connectedSegments);
                airLegs.add(leg);
                connectedSegments = new ArrayList<>();
            }

        }

        return airLegs;

    }


    private void parseAirAvailInfoChild(Segment seg, Node airSegmentNode) {
        //TODO add logging to the project

        //TODO parse BookingCodeInfo element for cabin classes
    }

    private void parseCodeshareChild(Segment seg, Node airSegmentNode) {
        Node codeshareInfo = XmlUtils.getNode("CodeshareInfo", airSegmentNode.getChildNodes());
        if (codeshareInfo != null) {
            seg.setOperatingAirlineName(XmlUtils.getNodeValue(codeshareInfo));
            if (codeshareInfo.hasAttributes()) {
                NamedNodeMap attrs = codeshareInfo.getAttributes();
                seg.setOperatingAirline(XmlUtils.getAttrByName(codeshareInfo, "OperatingCarrier"));
                seg.setOperatingFlightNumber(XmlUtils.getAttrByName(codeshareInfo, "OperatingFlightNumber"));
            }
        }
    }


    private FlightDetailsTravelport getFlightDetails(NamedNodeMap nodeAttributes) {

        FlightDetailsTravelport currentFly = new FlightDetailsTravelport();

        currentFly.setKey(nodeAttributes.getNamedItem("Key").getNodeValue().toString());

        currentFly.setOriginalTerminal(nodeAttributes.getNamedItem("OriginTerminal") == null ? ""
                : nodeAttributes.getNamedItem("OriginTerminal").getNodeValue().toString());

        currentFly.setDestinationTerminal(nodeAttributes.getNamedItem("DestinationTerminal") == null ? ""
                : nodeAttributes.getNamedItem("DestinationTerminal").getNodeValue().toString());

        currentFly.setFlightTime(Long.valueOf(nodeAttributes.getNamedItem("FlightTime").getNodeValue().toString()));
        currentFly.setEquipment(nodeAttributes.getNamedItem("Equipment").getNodeValue().toString());
        return currentFly;
    }

    private List<AirLeg> buildAirLegs(Map<String, Object> itinerary) {

        List<AirLeg> results = new LinkedList<>();
        return results;

    }

    private AirLeg getAirleg(JSONArray outboundFlights) {

        AirLeg leg = new AirLeg();
        return leg;
    }

    private Segment buildSegment(Map<String, Object> segmentMap) throws IOException, FarandulaException {

        Segment seg = new Segment();
        return seg;
    }

    private String getAuthEncoded() {

        Base64.Encoder base64 = Base64.getEncoder();
        String authTokenParam = base64.encodeToString((apiKey + ":" + apiPassword).getBytes());// --------- credentials
        return authTokenParam;

    }

}
