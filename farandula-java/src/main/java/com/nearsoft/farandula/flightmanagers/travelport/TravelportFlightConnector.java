package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightConnector;
import com.nearsoft.farandula.flightmanagers.travelport.request.xml.TravelportXMLRequest;
import com.nearsoft.farandula.models.*;
import com.nearsoft.farandula.utilities.CurrencyIATACodesHelper;
import com.nearsoft.farandula.utilities.XmlUtils;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.nearsoft.farandula.utilities.CabinClassParser.getCabinClassType;
import static com.nearsoft.farandula.utilities.LoggerUtils.getPrettyXML;

/**
 * Created by pruiz on 4/20/17.
 */
public class TravelportFlightConnector implements FlightConnector {

    //Logger
    private Logger LOGGER = LoggerFactory.getLogger(TravelportFlightConnector.class);
    private static String apiKey;
    private static String apiPassword;
    private static String targetBranch;
    private static Map<String, String> airlinesCodeMap = new HashMap<>();
    private static Map<String, Segment> segmentMap = new HashMap<>();
    private static String url_api = "";
    private static AtomicInteger idCount;

    static {
        Properties props = new Properties();
        try {
            url_api = "https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService";
            props.load(TravelportFlightConnector.class.getResourceAsStream("/config.properties"));
            apiKey = props.getProperty("travelport.apiUser");
            apiPassword = props.getProperty("travelport.apiPassword");
            targetBranch = props.getProperty("travelport.targetBranch");
            fillAirlinesMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        idCount = new AtomicInteger(0);
    }

    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();

    private static void fillAirlinesMap() throws IOException {
        Properties properties = new Properties();
        properties.load(TravelportFlightConnector.class.getResourceAsStream("/airlinesCode.properties"));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            airlinesCodeMap.put(key, value);
        }
    }

    @Override
    public List<Itinerary> getAvail(FlightsSearchCommand search) throws FarandulaException, IOException {


        SOAPMessage response = null;
        List<Itinerary> itineraries = new ArrayList<>();
        try {
            response = buildRequestForAvail(search);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.writeTo(out);
            String strResponse = new String(out.toByteArray());
            LOGGER.info("Travelport response: XML-BEGIN\n{}\nXML-END", getPrettyXML(strResponse));

            itineraries = parseAvailResponse(response, search);

            LOGGER.info("Travelport results:", itineraries);

            return itineraries;
        } catch (Exception e) {

            throwAndLogFactoryExceptions(e.getMessage(), ErrorType.AVAILABILITY_ERROR);

        }

        return itineraries;
    }


    public SOAPMessage buildRequestForAvail(FlightsSearchCommand search) throws IOException, SOAPException {

        //create SOAP envelope
        String envelope = buildEnvelopeStringFromSearch(search);

        LOGGER.info("Travelport request: XML-BEGIN\n{}\nXML-END", getPrettyXML(envelope));

        // Send SOAP Message to SOAP Server
        SOAPMessage message = buildSOAPMessage(envelope);

        SOAPMessage soapResponse = sendRequest(message);

        return soapResponse;
    }

    public SOAPMessage buildRequestForPricing(Segment seg, TravelportFlightDetails details) throws SOAPException, IOException {

        //create SOAP envelope
        String envelope = buildEnvelopeStringFromSegment(seg, details);

        // Send SOAP Message to SOAP Server
        SOAPMessage message = buildSOAPMessage(envelope);

        SOAPMessage soapResponse = sendRequest(message);

        return soapResponse;
    }

    private String buildEnvelopeStringFromSegment(Segment seg, TravelportFlightDetails details) {

        return TravelportXMLRequest.getRequest(seg, targetBranch, details);

    }

    public String buildEnvelopeStringFromSearch(FlightsSearchCommand search) {

        return TravelportXMLRequest.getRequest(search, targetBranch);

    }

    private SOAPMessage buildSOAPMessage(String envelope) throws SOAPException, IOException {

        MessageFactory factory = MessageFactory.newInstance();
        //adds envelope string to soapMessage
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(envelope.getBytes(Charset.forName("UTF-8"))));
        return message;

    }

    public SOAPMessage sendRequest(SOAPMessage message) throws SOAPException {

        MimeHeaders headers = message.getMimeHeaders();
        headers.addHeader("Authorization", "Basic " + getAuthEncoded());
        // Create SOAP Connection
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        SOAPMessage response = soapConnection.call(message, url_api);

        return response;
    }

    public List<Itinerary> parseAvailResponse(SOAPMessage response, FlightsSearchCommand searchCommand) throws IOException, SOAPException, ParseException {

        List<Itinerary> itinerariesList = new ArrayList<>();
        SOAPEnvelope env = response.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();

        idCount.set(0);

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

        //Air pricing solutions (Itinerary equivalent)
        NodeList solutions = body.getElementsByTagName("air:AirPricingSolution");

        //Map of all segments in xml
        createSegmentHashMap(list, resultFlightsDetails);

        for (int i = 0; i < solutions.getLength(); i++) {

            //Seat
            parseAirAvailInfoChild(solutions.item(i));

            NodeList legsNodes = ((Element)solutions.item(i)).getElementsByTagName("air:Journey");

            Itinerary itinerary = new Itinerary();
            List<AirLeg> legList = new ArrayList<>();
            itinerary.setPrice(parsingFare(solutions.item(i).getAttributes()));

            for(int j = 0; j < legsNodes.getLength(); j++){

                AirLeg leg = new AirLeg();

                Node legNode = legsNodes.item(j);

                NodeList segNodes = ((Element)legNode).getElementsByTagName("air:AirSegmentRef");

                List<Segment> segmentList = new ArrayList<>();

                for(int k = 0; k < segNodes.getLength(); k++){

                    String segmentKey = segNodes.item(k).getAttributes().getNamedItem("Key").getNodeValue().toString();

                    Segment seg = segmentMap.get(segmentKey);

                    segmentList.add(seg);
                }

                leg.setId( idCount.incrementAndGet() + "" );
                leg.setDepartureAirportCode( segmentList.get(0).getDepartureAirportCode() );
                leg.setDepartingDate(segmentList.get(0).getDepartureDate());
                leg.setArrivalAirportCode(segmentList.get(segmentList.size()-1).getArrivalAirportCode());
                leg.setArrivalDate(segmentList.get(segmentList.size()-1).getArrivalDate());
                leg.setSegments(segmentList);

                legList.add(leg);
            }


            itinerary.setAirlegs(legList);

            itinerariesList.add(itinerary);
        }


        return itinerariesList;

    }

    private Fares parsingFare(NamedNodeMap solution) {

        Fares fare = new Fares();
        String totalPriceString = solution.getNamedItem("TotalPrice") == null ? ""
                                : solution.getNamedItem("TotalPrice").getNodeValue().toString();
        String basePriceString = solution.getNamedItem("BasePrice") == null ? ""
                                : solution.getNamedItem("BasePrice").getNodeValue().toString();
        String taxesPriceString = solution.getNamedItem("Taxes") == null ? ""
                                : solution.getNamedItem("Taxes").getNodeValue().toString();
        fare.setTotalPrice(parsingPrice(totalPriceString));
        fare.setBasePrice(parsingPrice(basePriceString));
        fare.setTaxesPrice(parsingPrice(taxesPriceString));
        return fare;
    }

    private Price parsingPrice(String priceString) {
        if (priceString.equals("")) {
            return null;
        }
        Price price = new Price();
        String amount = priceString.substring(3);
        price.setAmount(Double.parseDouble(amount));
        price.setCurrencyCode(priceString.substring(0,2));
        return price;
    }

    private void createSegmentHashMap(NodeList list, List<TravelportFlightDetails> resultFlightsDetails) {

        for (int i = 0; i < list.getLength(); i++) {

            Node airSegmentNode = list.item(i);
            NamedNodeMap nodeAttributes = airSegmentNode.getAttributes();

            //Attribute with referenced key to flight details
            NodeList flightReference = ((Element) airSegmentNode).getElementsByTagName("air:FlightDetailsRef");
            NamedNodeMap flightDetailsAttributes = flightReference.item(0).getAttributes();
            TravelportFlightDetails flightDetails = findCorrespondingDeatils(flightDetailsAttributes.getNamedItem("Key").getNodeValue().toString(), resultFlightsDetails);

            flightDetails.setGroup(nodeAttributes.getNamedItem("Group").getNodeValue().toString());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            Segment seg = new Segment();
            seg.setKey(nodeAttributes.getNamedItem("Key").getNodeValue().toString());
            seg.setMarketingAirlineCode(nodeAttributes.getNamedItem("Carrier").getNodeValue().toString());
            seg.setMarketingAirlineName(airlinesCodeMap.get(seg.getMarketingAirlineCode()));
            seg.setMarketingFlightNumber(nodeAttributes.getNamedItem("FlightNumber").getNodeValue().toString());

            //operating airline data
            parseCodeshareChild(seg, airSegmentNode);

            seg.setAirplaneData(flightDetails.getEquipment());

            //departure data
            seg.setDepartureAirportCode(nodeAttributes.getNamedItem("Origin").getNodeValue().toString());
            seg.setDepartureTerminal(flightDetails.getOriginalTerminal());
            LocalDateTime departingDateTime = LocalDateTime.parse(
                    nodeAttributes.getNamedItem("DepartureTime").getNodeValue().toString(), formatter);
            seg.setDepartureDate(departingDateTime);

            //arrival data
            seg.setArrivalAirportCode(nodeAttributes.getNamedItem("Destination").getNodeValue().toString());
            seg.setArrivalTerminal(flightDetails.getDestinationTerminal());
            LocalDateTime arrivalDateTime = LocalDateTime.parse(
                    nodeAttributes.getNamedItem("ArrivalTime").getNodeValue().toString(), formatter);
            seg.setArrivalDate(arrivalDateTime);

            seg.setDuration(flightDetails.getFlightTime());

            segmentMap.put(seg.getKey(), seg);
        }
    }

    private TravelportFlightDetails findCorrespondingDeatils(String key, List<TravelportFlightDetails> detailsList) {

        Optional<TravelportFlightDetails> details = detailsList.stream()
                .filter(detail -> detail.getKey().equals(key))
                .findFirst();
        if (details.isPresent()) {
            return details.get();
        }
        return null;

    }

    private void getSegmentPrice(Segment seg, TravelportFlightDetails details) throws IOException, SOAPException {

        SOAPMessage pricingRespose = buildRequestForPricing(seg, details);

        parsePricingResponse(pricingRespose, seg);

    }

    private void parsePricingResponse(SOAPMessage response, Segment seg) throws SOAPException {

        SOAPEnvelope env = response.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();

        NodeList pricessList = body.getElementsByTagName("air:AirPricingSolution");
        Node node = pricessList.item(0);
        if (node != null) {
            Price base = CurrencyIATACodesHelper.buildPrice(XmlUtils.getAttrByName(node, "BasePrice"));
            Price taxes = CurrencyIATACodesHelper.buildPrice(XmlUtils.getAttrByName(node, "Taxes"));
            Price totalPrice = CurrencyIATACodesHelper.buildPrice(XmlUtils.getAttrByName(node, "TotalPrice"));
            Fares fares = new Fares();
            fares.setBasePrice(base);
            fares.setTaxesPrice(taxes);
            fares.setTotalPrice(totalPrice);

        } else {
            LOGGER.warn("The segment: " + seg.toString() + " does not have prices.");
        }
    }


    private void parseAirAvailInfoChild(Node airPricingSolution) {

        NodeList airPricingInfo = ((Element)airPricingSolution).getElementsByTagName("air:AirPricingInfo");

        if( airPricingInfo.getLength() == 0 )
            return;

        List<Node> bookingList = XmlUtils.getNodeList("air:BookingInfo", airPricingInfo.item(0).getChildNodes());

        for (Node book : bookingList){
            String segmentKey = ((Element) book).getAttribute("SegmentRef");

            Segment actualSeg = segmentMap.get(segmentKey);

            if( actualSeg.getSeatsAvailable() == null || actualSeg.getSeatsAvailable().size() == 0 ){

                int bookingCount = Integer.parseInt(((Element) book).getAttribute("BookingCount"));
                String cabinClass = ((Element) book).getAttribute("CabinClass");

                List<Seat> seatList = new ArrayList<>();

                for(int i = 0; i < bookingCount; i++){
                    Seat seat = new Seat();
                    seat.setClassCabin(getCabinClassType(cabinClass));
                    //Current Travelport Response does not include place for seat
                    seat.setPlace("");
                    seatList.add(seat);
                }

                actualSeg.setSeatsAvailable(seatList);
            }
        }
    }

    private void parseCodeshareChild(Segment seg, Node airSegmentNode) {
        Node codeshareInfo = XmlUtils.getNode("air:CodeshareInfo", airSegmentNode.getChildNodes());
        if (codeshareInfo != null) {
            seg.setOperatingAirlineName(XmlUtils.getNodeValue(codeshareInfo));
            if (codeshareInfo.hasAttributes()) {
                seg.setOperatingAirlineCode(XmlUtils.getAttrByName(codeshareInfo, "OperatingCarrier"));
                seg.setOperatingAirlineName(airlinesCodeMap.get(seg.getOperatingAirlineCode()));
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

        currentFly.setFlightTime(
                (nodeAttributes.getNamedItem("FlightTime") == null)
                ? 0L : Long.valueOf(nodeAttributes.getNamedItem("FlightTime").getNodeValue().toString())
        );
        currentFly.setEquipment(
                (nodeAttributes.getNamedItem("Equipment") == null)
                ? "" : nodeAttributes.getNamedItem("Equipment").getNodeValue().toString()
        );
        return currentFly;
    }

    private String getAuthEncoded() {

        Base64.Encoder base64 = Base64.getEncoder();
        String authTokenParam = base64.encodeToString((apiKey + ":" + apiPassword).getBytes());// --------- credentials
        return authTokenParam;

    }

    public static String getTargetBranch() {
        return targetBranch;
    }


    private void throwAndLogFactoryExceptions(String message, ErrorType type) throws FarandulaException {
        LOGGER.error(message, FarandulaException.class);
        throw new FarandulaException(type, message);
    }
}
