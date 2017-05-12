package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.flightmanagers.travelport.request.xml.TravelportXMLRequest;
import com.nearsoft.farandula.models.*;
import com.nearsoft.farandula.utilities.XmlUtils;
import net.minidev.json.JSONArray;
import okhttp3.OkHttpClient;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import javax.xml.soap.*;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.nearsoft.farandula.utilities.CabinClassParser.getCabinClassType;

/**
 * Created by pruiz on 4/20/17.
 */
public class TravelportFlightManager implements FlightManager {

    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();

    private static String apiKey;

    private static String apiPassword;

    private static String targetBranch;

    private static Map<String, String> airlinesCodeMap = new HashMap<>();

    private static String url_api= "";

    static {
        Properties props = new Properties();
        try {
            url_api = "https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService";
            props.load(TravelportFlightManager.class.getResourceAsStream("/config.properties"));
            apiKey = props.getProperty("travelport.apiUser");
            apiPassword = props.getProperty("travelport.apiPassword");
            targetBranch = props.getProperty("travelport.targetBranch");
            fillAirlinesMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fillAirlinesMap() throws IOException {
        Properties properties = new Properties();
        properties.load( TravelportFlightManager.class.getResourceAsStream("/airlinesCode.properties"));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            airlinesCodeMap.put(key, value);
        }
    }

    @Override
    public List<Itinerary> getAvail(SearchCommand search) throws FarandulaException {

        try {
            SOAPMessage request = buildRequestForAvail(search);
            List<AirLeg> airLegs = parseAvailResponse(request, search);
            Itinerary itinerary = new Itinerary();
            itinerary.setAirlegs( airLegs );
            List<Itinerary> itinerariesList = new ArrayList<>();
            itinerariesList.add( itinerary );
            return itinerariesList;

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    public SOAPMessage buildRequestForAvail(SearchCommand search) throws SOAPException, IOException {

        //create SOAP envelope
        String envelope = buildEnvelopeStringFromSearch(search);

        // Send SOAP Message to SOAP Server
        SOAPMessage message = buildSOAPMessage(envelope);

        SOAPMessage soapResponse = sendRequest(message);

        return soapResponse;
    }

    public SOAPMessage buildRequestForPricing(Segment seg, TravelportFlightDetails details) throws SOAPException, IOException {

        //create SOAP envelope
        String envelope = buildEnvelopeStringFromSegment(seg, details );

        // Send SOAP Message to SOAP Server
        SOAPMessage message = buildSOAPMessage(envelope);

        SOAPMessage soapResponse = sendRequest(message);

        return soapResponse;
    }

    private String buildEnvelopeStringFromSegment(Segment seg, TravelportFlightDetails details) {

        return TravelportXMLRequest.getRequest( seg , targetBranch, details );

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

    public SOAPMessage sendRequest(SOAPMessage message ) throws SOAPException {

        MimeHeaders headers = message.getMimeHeaders();
        headers.addHeader("Authorization", "Basic " + getAuthEncoded());
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        SOAPMessage response = soapConnection.call(message, url_api);

        return response;
    }

    public List<AirLeg> parseAvailResponse(SOAPMessage response, SearchCommand searchCommand) throws IOException, SOAPException, ParseException {

        List<AirLeg> airLegs = new ArrayList<>();
        SOAPEnvelope env = response.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();

        //flights
        NodeList listFlights = body.getElementsByTagName("air:FlightDetails");
        List<TravelportFlightDetails> resultFlightsDetails = new LinkedList<>();
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
            resultFlightsDetails.get(i).setGroup( nodeAttributes.getNamedItem("Group").getNodeValue().toString() );

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            Segment seg = new Segment();
            seg.setAirlineIconPath("");
            seg.setKey( nodeAttributes.getNamedItem("Key").getNodeValue().toString() );
            seg.setMarketingAirlineCode(nodeAttributes.getNamedItem("Carrier").getNodeValue().toString());
            seg.setMarketingAirlineName( airlinesCodeMap.get( seg.getMarketingAirlineCode() ) );
            seg.setMarketingFlightNumber(nodeAttributes.getNamedItem("FlightNumber").getNodeValue().toString());

            //operating airline data
            parseCodeshareChild(seg, airSegmentNode);

            //passengers
            parseAirAvailInfoChild(seg, airSegmentNode);

            seg.setAirplaneData(resultFlightsDetails.get(i).getEquipment());

            //departure data
            seg.setDepartureAirportCode(nodeAttributes.getNamedItem("Origin").getNodeValue().toString());
            seg.setDepartureTerminal(resultFlightsDetails.get(i).getOriginalTerminal());
            LocalDateTime departingDateTime = LocalDateTime.parse(
                    nodeAttributes.getNamedItem("DepartureTime").getNodeValue().toString(), formatter);
            seg.setDepartureDate(departingDateTime);

            //arrival data
            seg.setArrivalAirportCode(nodeAttributes.getNamedItem("Destination").getNodeValue().toString());
            seg.setArrivalTerminal(resultFlightsDetails.get(i).getDestinationTerminal());
            LocalDateTime arrivalDateTime = LocalDateTime.parse(
                    nodeAttributes.getNamedItem("ArrivalTime").getNodeValue().toString(), formatter);
            seg.setArrivalDate(arrivalDateTime);

            seg.setDuration(resultFlightsDetails.get(i).getFlightTime());

            getSegmentPrice( seg, resultFlightsDetails.get( i ) );

            connectedSegments.add( seg );

            //case when the last segment arrival is the search arrival airport OR
            //case for the returning airleg, i.e case when the arrival segment is the departure airport in search
            if ( seg.getArrivalAirportCode().equals( searchCommand.getArrivalAirport() )  ||
                    seg.getArrivalAirportCode().equals( searchCommand.getDepartureAirport() )
                    ){

                AirLeg leg = new AirLeg();
                leg.setId("tempID");
                leg.setDepartureAirportCode(connectedSegments.get(0).getDepartureAirportCode());
                leg.setDepartingDate(connectedSegments.get(0).getDepartureDate());
                leg.setArrivalAirportCode(connectedSegments.get( connectedSegments.size() - 1).getArrivalAirportCode());
                leg.setArrivalDate(connectedSegments.get(connectedSegments.size() - 1).getArrivalDate());
                leg.setSegments(connectedSegments);
                airLegs.add(leg);
                connectedSegments = new ArrayList<>();
            }

        }

        return airLegs;

    }

    private void getSegmentPrice(Segment seg, TravelportFlightDetails details) throws IOException, SOAPException {

        SOAPMessage pricingRespose = buildRequestForPricing(seg, details );

        parsePricingResponse( pricingRespose, seg );

    }

    private void parsePricingResponse(SOAPMessage response, Segment seg) throws SOAPException {

        SOAPEnvelope env = response.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();

        NodeList pricessList = body.getElementsByTagName("air:AirPricingSolution");
        Node node =  pricessList.item(0);
        if ( node !=null ) {
            String base = XmlUtils.getAttrByName(node, "BasePrice");
            String taxes = XmlUtils.getAttrByName(node, "Taxes");
            String totalPrice = XmlUtils.getAttrByName(node, "TotalPrice");
            Price price = new Price();
            price.setBasePrice( Double.parseDouble( base ) );
            price.setTaxesPrice( Double.parseDouble( taxes ) );
            price.setTotalPrice( Double.parseDouble( totalPrice ) );

        }else{
            Logger.getGlobal().log(Level.WARNING , "The segment: " + seg.toString() + " does not have prices." );
        }
    }


    private void parseAirAvailInfoChild(Segment seg, Node airSegmentNode) {
        //TODO add logging to the project

        Node airAvailInfo = XmlUtils.getNode("air:AirAvailInfo", airSegmentNode.getChildNodes());
        if (airAvailInfo != null) {
            List<Node> bookingCodeInfo = XmlUtils.getNodeList("air:BookingCodeInfo", airAvailInfo.getChildNodes());
            List<Seat> seatsResult = new ArrayList<>();
            for (Node node : bookingCodeInfo ){

                String classCabin = XmlUtils.getAttrByName(node, "CabinClass");
                String bookingCounts = XmlUtils.getAttrByName(node, "BookingCounts");

                for ( String seatKey : bookingCounts.split("\\|")  ){
                    Seat seat =  new Seat();
                    seat.setClassCabin( getCabinClassType( classCabin) );
                    seat.setPlace( seatKey  );
                    seatsResult.add( seat );
                }

            }
            seg.setSeatsAvailable( seatsResult );
        }

    }

    private void parseCodeshareChild(Segment seg, Node airSegmentNode) {
        Node codeshareInfo = XmlUtils.getNode("air:CodeshareInfo", airSegmentNode.getChildNodes());
        if (codeshareInfo != null) {
            seg.setOperatingAirlineName(XmlUtils.getNodeValue(codeshareInfo));
            if (codeshareInfo.hasAttributes()) {
                seg.setOperatingAirlineCode(XmlUtils.getAttrByName(codeshareInfo, "OperatingCarrier"));
                seg.setOperatingAirlineName( airlinesCodeMap.get( seg.getOperatingAirlineCode() ) );
                seg.setOperatingFlightNumber(XmlUtils.getAttrByName(codeshareInfo, "OperatingFlightNumber"));
            }
        }
    }


    private TravelportFlightDetails getFlightDetails(NamedNodeMap nodeAttributes) {

        TravelportFlightDetails currentFly = new TravelportFlightDetails();

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
