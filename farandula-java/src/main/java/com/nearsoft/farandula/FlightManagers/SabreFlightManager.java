package com.nearsoft.farandula.FlightManagers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nearsoft.farandula.Auth.AccessManager;
import com.nearsoft.farandula.Auth.AuthInterceptor;
import com.nearsoft.farandula.Auth.Creds;
import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.Airleg;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;
import com.nearsoft.farandula.models.Segment;
import com.nearsoft.farandula.utilities.GMTFormatter;
import net.minidev.json.JSONArray;
import okhttp3.*;
import com.nearsoft.farandula.requests.json.sabre.SabreJSONRequest;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//TODO consider create an specific trip manager for each API or create a connector/plugin framework
public class SabreFlightManager implements FlightManager {

    //TODO should we use an HTTP client lib or its better to do it bare bones  (ProofOfConcept) pros and cons?
    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();
    private final AccessManager _accessManager;
    private static Map<String, String> codeToClassMap = new HashMap<>();

    public SabreFlightManager(Creds creds) {
        _accessManager = new AccessManager(creds);
    }

    public static SabreFlightManager prepareSabre() throws IOException, FarandulaException {

        Properties props = new Properties();
        props.load(SabreFlightManager.class.getResourceAsStream("/config.properties"));
        final Creds creds = new Creds(props.getProperty("sabre.client_id"), props.getProperty("sabre.client_secret"));
        SabreFlightManager tripManager = new SabreFlightManager(creds);
        return tripManager;

    }

    private static void fillCodeToClassMap( ) throws IOException {

        Properties properties = new Properties();
        properties.load(SabreFlightManager.class.getResourceAsStream("/Sabre/cabinCodes.properties"));
        for(String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            codeToClassMap.put( key, value);
        }

    }

    private OkHttpClient createHttpClient() throws FarandulaException {
        if (_builder.interceptors().isEmpty()) {
            _builder.addInterceptor(new AuthInterceptor(_accessManager.getAccessToken()));
            _builder.connectTimeout(1, TimeUnit.MINUTES);
            _builder.readTimeout(1, TimeUnit.MINUTES);
        }
        return _builder.build();
    }

    @Override
    public List<Flight> getAvail(SearchCommand search) throws FarandulaException {

        try {
            Request request = buildRequestForAvail(search);
            InputStream responseStream = sendRequest(request);
            Stream<Flight> flightStream = parseAvailResponse(responseStream);

            //TODO consider here add some hook capabilities to post-process the stream
            return flightStream.collect(Collectors.toCollection( LinkedList::new ) );

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }


    private Request buildRequestForAvail( SearchCommand search ) throws IOException {
        final Request.Builder builder = new Request.Builder();

        if ( search.getOffSet() > 0) {
            builder.url("https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=" + search.getOffSet() + "&offset=1");
        } else {
            builder.url("https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1");
        }

        String jsonRequest = buildJsonFromSearch( search );

        builder.post(RequestBody.create(MediaType.parse("application/json"), jsonRequest));
        return builder.build();
    }

    InputStream sendRequest(Request request) throws IOException, FarandulaException {
        final Response response = createHttpClient().newCall(request).execute();
        return response.body().byteStream();
    }

    public Stream<Flight> parseAvailResponse(InputStream response) throws IOException {

        fillCodeToClassMap();
        ReadContext ctx = JsonPath.parse(response);
        JSONArray pricedItineraries = ctx.read("$..PricedItinerary[*]");
        Stream<Flight> flightStream = pricedItineraries
                .stream()
                .map(pricedItinerary -> {

                    List<Airleg> legs = buildAirLegs((Map<String, Object>) pricedItinerary);
                    JSONArray airItineraryPricingInfo = (JSONArray) getValueOf(pricedItinerary, "AirItineraryPricingInfo");
                    if ( airItineraryPricingInfo.size() >1  ){

                        throw new RuntimeException("We don't support multiple AirPricingInfo");
                    }

                    ArrayList<List<String>> cabinsBySegment = extractCabinsInfo(airItineraryPricingInfo);
                    int cabinIndex = 0;
                    for (Airleg leg : legs) {
                        for (Segment segment : leg.getSegments()) {
                            segment.setTravelClass( ConvertCodeToClass( cabinsBySegment.get(0).get( cabinIndex ) ) );
                            cabinIndex++;
                        }
                    }

                    Flight currentFly = new Flight();
                    currentFly.setLegs(legs);
                    currentFly.setId(getValueOf( pricedItinerary, "SequenceNumber").toString());

                    return currentFly;

                });

        return  flightStream;

    }

    private String ConvertCodeToClass(String s) {

        if ( codeToClassMap.containsKey( s ) ){
            return codeToClassMap.get( s );
        }

        return "Other";
    }

    private ArrayList<List<String>> extractCabinsInfo(JSONArray airItineraryPricingInfoMap) {

        ArrayList<List<String>> cabinsBySegment = airItineraryPricingInfoMap
                .stream()
                .map(itineraryPricing -> {

                    JSONArray fareInfosArray = getValueOf(itineraryPricing, "FareInfos.FareInfo", JSONArray.class);
                    List<String> cabins = fareInfosArray
                            .stream()
                            .map(fareInfo -> {
                                return getValueOf(fareInfo, "TPA_Extensions.Cabin.Cabin", String.class);
                            })
                            .collect(Collectors.toCollection(ArrayList::new));

                    return cabins;
                }).collect(Collectors.toCollection(ArrayList::new));

        return cabinsBySegment;
    }


    //TODO extract this methods to a helper class
    private Object getValueOf(Object stringObjectMap, String keyName) {

        return  getValueOf(stringObjectMap, keyName,Object.class);
    }

    private <T> T getValueOf(Object stringObjectMap, String keyName, Class<T> type) {
        int indexSeparator = keyName.indexOf(".");
        if (indexSeparator == -1 ){
            Map<String, Object> theMap = (Map<String, Object>) stringObjectMap;

            return type.cast(theMap.get(keyName));
        }else{
            String currentKey = keyName.substring(0, indexSeparator);
            String newKeyPath = keyName.substring(indexSeparator+1, keyName.length() );
            Object currentMap = ((Map<String, Object>) stringObjectMap).get(currentKey);
            return getValueOf(currentMap, newKeyPath,type);
        }

    }

    private List<Airleg> buildAirLegs( Map<String, Object> pricedItinerary) {

        Map<String, Object> airItinerary = (Map<String, Object>)getValueOf(pricedItinerary, "AirItinerary");
        Map<String, Object> originDestinationOptions = (Map<String, Object>) getValueOf(airItinerary, "OriginDestinationOptions");
        JSONArray originDestinationOption = (JSONArray) getValueOf(originDestinationOptions, "OriginDestinationOption");

        return originDestinationOption
                .stream()
                .map( option -> {
                    JSONArray jsonSegmentArray = (JSONArray) getValueOf( option, "FlightSegment");
                    return jsonSegmentArray.stream()
                            .map(g ->  buildSegment((Map<String, Object>) g))
                            .collect( Collectors.toCollection( LinkedList::new  ));
                })
                .filter( segments -> !segments.isEmpty() )
                .map( segments -> {
                    Airleg leg = new Airleg();
                    leg.setId("tempID");
                    leg.setDepartureAirportCode(segments.get(0).getDepartureAirportCode());
                    leg.setDepartingDate(segments.get(0).getDepartingDate());
                    leg.setArrivalAirportCode(segments.get(segments.size() - 1).getArrivalAirportCode());
                    leg.setArrivalDate(segments.get(segments.size() - 1).getArrivalDate());
                    leg.setSegments(segments);
                    return leg;

                })
                .collect( Collectors.toCollection( LinkedList::new  ) );

    }

    private Segment buildSegment( Map<String, Object> g) {
        Map<String, Object > segmentMap = g;
        //Todo change segment id and PATH
        //airline
        JSONArray jsonEquipmentArray = (JSONArray) getValueOf(segmentMap, "Equipment");
        Map<String, Object > equipmentData = (Map<String, Object>) jsonEquipmentArray.get(0);
        Map<String, Object > operativeAirlineData = (Map<String, Object>) getValueOf(segmentMap, "OperatingAirline");
        Map<String, Object > marketingAirlineData = (Map<String, Object>) getValueOf(segmentMap, "MarketingAirline");

        //departure
        Map<String, Object > departureAirportData = (Map<String, Object>) getValueOf(segmentMap, "DepartureAirport");
        Map<String, Object > departureTimeZone = (Map<String, Object>) getValueOf(segmentMap, "DepartureTimeZone");
        //arrival
        Map<String, Object > arrivalAirportData = (Map<String, Object>) getValueOf(segmentMap, "ArrivalAirport");
        Map<String, Object > arrivalTimeZone = (Map<String, Object>) getValueOf(segmentMap, "ArrivalTimeZone");

        //flight data
        Segment seg = new Segment();
        seg.setAirlineIconPath("");
        seg.setOperatingAirline( (String) getValueOf(operativeAirlineData, "Code"));
        seg.setMarketingAirline( (String) getValueOf(marketingAirlineData, "Code"));
        seg.setFlightNumber( (String) getValueOf(segmentMap, "FlightNumber"));
        seg.setAirplaneData( (String) getValueOf(equipmentData, "AirEquipType"));
        //TODO travel class for SABRE
        seg.setTravelClass("");

        //departure info
        seg.setDepartureAirportCode( (String) getValueOf(departureAirportData, "LocationCode"));
        seg.setDepartureTerminal( (String) getValueOf(departureAirportData, "TerminalID"));
        LocalDateTime departureDateTime = LocalDateTime.parse (
                (String) getValueOf(segmentMap, "DepartureDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setDepartingDate( departureDateTime );

        //arrival info
        seg.setArrivalAirportCode( (String) getValueOf(arrivalAirportData, "LocationCode"));
        seg.setArrivalTerminal( (String) getValueOf(arrivalAirportData, "TerminalID"));
        LocalDateTime arrivalDateTime = LocalDateTime.parse (
                (String) getValueOf(segmentMap, "ArrivalDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setArrivalDate( arrivalDateTime );

        //to obtain the flight time of this segment.
        long diffInMinutes = 0;
        if ( getValueOf(departureTimeZone, "GMTOffset").equals(getValueOf(arrivalTimeZone, "GMTOffset")) ){
            diffInMinutes = Duration.between( departureDateTime, arrivalDateTime )
                    .toMinutes();
        }else{
            String GMT_ZONE_departure = getValueOf(departureTimeZone, "GMTOffset").toString();
            String GMT_ZONE_arrival = getValueOf(arrivalTimeZone, "GMTOffset").toString();
            Instant timeStampDeparture = departureDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter( GMT_ZONE_departure )) );
            Instant timeStampArrival = arrivalDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter( GMT_ZONE_arrival )) );
            diffInMinutes = Duration.between( timeStampDeparture, timeStampArrival ).toMinutes();
        }
        seg.setDuration( diffInMinutes );

        return seg;
    }

    public static String buildJsonFromSearch(SearchCommand search) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //SabreJSONRequest.getRequest helps to create the JSON request depending of the search object
        JsonNode rootNode = mapper.readTree( SabreJSONRequest.getRequest( search ) );

        return rootNode.toString();

    }

}
