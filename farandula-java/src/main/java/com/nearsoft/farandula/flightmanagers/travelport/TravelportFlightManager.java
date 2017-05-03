package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.*;
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
    public List<Flight> getAvail(SearchCommand search) throws FarandulaException {

        try {
            SOAPMessage request = buildRequestForAvail(search);
            List<Flight> flightList = parseAvailResponse( request ) ;
            return flightList;

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    public SOAPMessage buildRequestForAvail(SearchCommand search) throws SOAPException, IOException {

        //create SOAP envelope
        String envelope =  buildEnvelopeStringFromSearch( search ) ;

        // Send SOAP Message to SOAP Server
        SOAPMessage message = buildSOAPMessage( envelope );

        String url_api = "https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService";
        SOAPMessage soapResponse = sendRequest( message, url_api );

        return soapResponse;
    }

    public String buildEnvelopeStringFromSearch(SearchCommand search) {

        Map valuesMap = new HashMap();
        valuesMap.put("departureAirport",  search.getDepartureAirport()  );
        valuesMap.put("arrivalAirport",  search.getArrivalAirport() );
        valuesMap.put("passengersNumber",  search.getPassengers().size() );
        valuesMap.put("departureDate", search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE) );
        valuesMap.put("returningDate", search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        valuesMap.put("targetBranch", targetBranch);
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        InputStream soapInputStream = this.getClass().getResourceAsStream("/travelport/XML.request/AirAvailability_Rq.xml");

        String soapEnvelope = new BufferedReader(new InputStreamReader( soapInputStream) )
                .lines()
                .collect(Collectors.joining("\n") );

        return sub.replace(soapEnvelope);

    }

    private SOAPMessage buildSOAPMessage(String envelope) throws SOAPException, IOException  {

        MessageFactory factory = MessageFactory.newInstance();
        //adds envelope string to soapMessage
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(envelope.getBytes(Charset.forName("UTF-8"))));
        return message;

    }

    public SOAPMessage sendRequest(SOAPMessage message, String url_api) throws SOAPException {

        MimeHeaders headers = message.getMimeHeaders();
        headers.addHeader( "Authorization", "Basic " + getAuthEncoded() );
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        return  soapConnection.call( message , url_api);
    }

    public List<Flight> parseAvailResponse(SOAPMessage response) throws IOException, SOAPException, ParseException {

        List<Flight> resultFlightsList = new ArrayList<>();
        SOAPEnvelope env= response.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();

        //for connections
        NodeList listItinerary = body.getElementsByTagName("air:Connection");
        List<Integer> connectionList = new ArrayList<>();
        for ( int i = 0 ; i < listItinerary.getLength() ; i++ ){
            Node currentNode = listItinerary.item(i);
            NamedNodeMap nodeAttributes = currentNode.getAttributes();
            connectionList.add( Integer.valueOf(nodeAttributes.getNamedItem("SegmentIndex").getNodeValue().toString()) );
        }

        //flights
        NodeList listFlights = body.getElementsByTagName("air:FlightDetails");
        List<FlightDetailsTravelport> resultFlightsDetails = new LinkedList<>();
        for ( int i = 0 ; i < listFlights.getLength() ; i++ ){
            Node currentNode = listFlights.item(i);
            NamedNodeMap nodeAttributes = currentNode.getAttributes();
            resultFlightsDetails.add( getFlightDetails( nodeAttributes ) );
        }

        //segments
        NodeList list = body.getElementsByTagName("air:AirSegment");
        List<Segment> connectedSegments =  new ArrayList<>();
        List<Airleg> airLegs = new ArrayList<>();
        for ( int i = 0 ; i < list.getLength() ; i++ ){

            Node currentNode = list.item(i);
            NamedNodeMap nodeAttributes = currentNode.getAttributes();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            Segment seg = new Segment();
            seg.setAirlineIconPath("");
            seg.setMarketingAirline( nodeAttributes.getNamedItem("Carrier").getNodeValue().toString()  );
            seg.setMarketingFlightNumber( nodeAttributes.getNamedItem("FlightNumber").getNodeValue().toString() );

            NamedNodeMap codeshareInfo = null;
            NodeList listNodes = currentNode.getChildNodes();
            for ( int j = 0; j < listNodes.getLength() ; j++ ) {
                if ( listNodes.item(j).getNodeName().equals("air:CodeshareInfo")) {
                    codeshareInfo = currentNode.getFirstChild().getAttributes();
                    if (codeshareInfo.getLength() == 0) {
                        seg.setOperatingAirline(codeshareInfo.getNamedItem("OperatingCarrier").getNodeValue().toString());
                    } else {
                        seg.setOperatingAirline(codeshareInfo.getNamedItem("OperatingCarrier").getNodeValue().toString());
                        seg.setOperatingFlightNumber(codeshareInfo.getNamedItem("OperatingFlightNumber").getNodeValue().toString());

                    }
                }
            }

            //TODO travel class for travelport
            seg.setTravelClass("");
            seg.setAirplaneData( resultFlightsDetails.get( i ).getEquipment() );

            //departure data
            seg.setDepartureAirportCode( nodeAttributes.getNamedItem("Origin").getNodeValue().toString() );
            seg.setDepartureTerminal( resultFlightsDetails.get( i ).getOriginalTerminal() );
            LocalDateTime departingDateTime = LocalDateTime.parse (
                    nodeAttributes.getNamedItem("DepartureTime").getNodeValue().toString(),formatter );
            seg.setArrivalDate( departingDateTime );

            //arrival data
            seg.setArrivalAirportCode( nodeAttributes.getNamedItem("Destination").getNodeValue().toString() );
            seg.setArrivalTerminal( resultFlightsDetails.get( i ).getDestinationTerminal() );
            LocalDateTime arrivalDateTime = LocalDateTime.parse (
                    nodeAttributes.getNamedItem("ArrivalTime").getNodeValue().toString(), formatter );
            seg.setArrivalDate( arrivalDateTime );

            seg.setDuration( resultFlightsDetails.get( i ).getFlightTime() );

            if ( connectionList.contains( i ) ){
                connectedSegments.add(seg);
            }else{

                //todo check with round trip
                //this case is for of one way trip
                Airleg leg = new Airleg();
                leg.setSegments( connectedSegments );
                airLegs.add( leg );
                Flight currentFlight = new Flight();
                currentFlight.setId( resultFlightsDetails.get( i ).getKey() );
                currentFlight.setLegs(  airLegs );

                resultFlightsList.add( currentFlight );

                airLegs.clear();
                connectedSegments.clear();
            }


        }

        return resultFlightsList;

    }

    private FlightDetailsTravelport getFlightDetails(NamedNodeMap nodeAttributes) {

        FlightDetailsTravelport currentFly = new FlightDetailsTravelport();

        currentFly.setKey( nodeAttributes.getNamedItem("Key").getNodeValue().toString()  );

        currentFly.setOriginalTerminal( nodeAttributes.getNamedItem("OriginTerminal") == null ? ""
                : nodeAttributes.getNamedItem("OriginTerminal").getNodeValue().toString()  );

        currentFly.setDestinationTerminal( nodeAttributes.getNamedItem("DestinationTerminal") == null ? ""
                : nodeAttributes.getNamedItem("DestinationTerminal").getNodeValue().toString() );

        currentFly.setFlightTime( Long.valueOf(nodeAttributes.getNamedItem("FlightTime").getNodeValue().toString()) );
        currentFly.setEquipment( nodeAttributes.getNamedItem("Equipment").getNodeValue().toString() );
        return currentFly;
    }

    private List<Airleg> buildAirLegs(Map<String, Object> itinerary) {

        List<Airleg> results = new LinkedList<>();
        return results;

    }

    private Airleg getAirleg(JSONArray outboundFlights) {

        Airleg leg = new Airleg();
        return leg;
    }

    private Segment buildSegment(Map<String, Object> segmentMap) throws IOException, FarandulaException {

        Segment seg = new Segment();
        return seg;
    }

    private String getAuthEncoded() {

        Base64.Encoder base64 = Base64.getEncoder();
        String authTokenParam =  base64.encodeToString(( apiKey + ":" + apiPassword).getBytes());// --------- credentials
        return authTokenParam;

    }

}