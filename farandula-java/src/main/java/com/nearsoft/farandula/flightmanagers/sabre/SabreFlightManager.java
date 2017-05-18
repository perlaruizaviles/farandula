package com.nearsoft.farandula.flightmanagers.sabre;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nearsoft.farandula.auth.AccessManager;
import com.nearsoft.farandula.auth.AuthInterceptor;
import com.nearsoft.farandula.auth.Creds;
import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.flightmanagers.sabre.request.json.SabreJSONRequest;
import com.nearsoft.farandula.models.*;
import com.nearsoft.farandula.utilities.GMTFormatter;
import net.minidev.json.JSONArray;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.nearsoft.farandula.utilities.CabinClassParser.getCabinClassType;
import static com.nearsoft.farandula.utilities.NestedMapsHelper.getValueOf;

public class SabreFlightManager implements FlightManager {

    private static Map<String, String> codeToClassMap = new HashMap<>();
    private static Map<String, String> airlinesCodeMap = new HashMap<>();
    private static String clientId;
    private static String clientSecret;

    static {

        Properties props = new Properties();
        try {
            props.load(SabreFlightManager.class.getResourceAsStream("/config.properties"));
            clientId = props.getProperty("sabre.client_id");
            clientSecret = props.getProperty("sabre.client_secret");
            fillReferenceMaps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO should we use an HTTP client lib or its better to do it bare bones  (ProofOfConcept) pros and cons?
    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();
    private final AccessManager _accessManager;

    public SabreFlightManager() {
        Creds creds = new Creds(clientId, clientSecret);
        _accessManager = new AccessManager(creds);
    }

    private static void fillReferenceMaps() throws IOException {

        Properties properties = new Properties();
        properties.load(SabreFlightManager.class.getResourceAsStream("/Sabre/cabinCodes.properties"));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            codeToClassMap.put(key, value);
        }

        properties.clear();

        properties.load(SabreFlightManager.class.getResourceAsStream("/airlinesCode.properties"));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            airlinesCodeMap.put(key, value);
        }

    }

    public static String buildJsonFromSearch(SearchCommand search) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //SabreJSONRequest.getRequest helps to create the JSON request depending of the search object
        JsonNode rootNode = mapper.readTree(SabreJSONRequest.getRequest(search));

        return rootNode.toString();

    }

    private OkHttpClient createHttpClient() throws FarandulaException {
        if (_builder.interceptors().isEmpty()) {
            _builder.addInterceptor(new AuthInterceptor(_accessManager.getAccessToken()));
            _builder.connectTimeout(2, TimeUnit.MINUTES);
            _builder.readTimeout(2, TimeUnit.MINUTES);
        }
        return _builder.build();
    }

    @Override
    public List<Itinerary> getAvail(SearchCommand search) throws FarandulaException {

        try {
            Request request = buildRequestForAvail(search);
            InputStream responseStream = sendRequest(request);
            return parseAvailResponse(responseStream, search);

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    private Request buildRequestForAvail(SearchCommand search) throws IOException {
        final Request.Builder builder = new Request.Builder();

        if (search.getOffSet() > 0) {
            builder.url("https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&enabletagging=true&limit=" + search.getOffSet() + "&offset=1");
        } else {
            builder.url("https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&enabletagging=true&limit=50&offset=1");
        }

        String jsonRequest = buildJsonFromSearch(search);

        builder.post(RequestBody.create(MediaType.parse("application/json"), jsonRequest));
        return builder.build();
    }

    public InputStream sendRequest(Request request) throws IOException, FarandulaException {
        final Response response = createHttpClient().newCall(request).execute();
        return response.body().byteStream();
    }

    public List<Itinerary> parseAvailResponse(InputStream response, SearchCommand searchCommand) throws IOException, FarandulaException {

        ReadContext ctx = JsonPath.parse(response);
        checkForErrors( ctx );

        JSONArray pricedItineraries = ctx.read("$..PricedItinerary[*]");
        
        List<Itinerary> itineraries = new ArrayList<>();
        for (Object pricedItinerary : pricedItineraries) {

            Itinerary itinerary = buildAirLegs((Map<String, Object>) pricedItinerary, searchCommand);

            JSONArray airItineraryPricingInfo = (JSONArray) getValueOf(pricedItinerary, "AirItineraryPricingInfo");
            if (airItineraryPricingInfo.size() > 1) {
                throw new RuntimeException("We don't support multiple AirPricingInfo");
            }

            ArrayList<List<Map>> cabinsBySegment = extractCabinsInfo(airItineraryPricingInfo);
            getSeats(itinerary.getAirlegs().get(0), cabinsBySegment);

            if ( searchCommand.getType() == FlightType.ROUNDTRIP )
                getSeats(itinerary.getAirlegs().get(1), cabinsBySegment);

            //getting prices
            Fares faresIti = extractFaresInfo(airItineraryPricingInfo);
            itinerary.setPrice(faresIti);

            itineraries.add(itinerary);
        }

        return itineraries;

    }

    private void checkForErrors(ReadContext ctx) throws FarandulaException {
        if ( ctx.read( "$..Errors[*]" ) != null && ((JSONArray)ctx.read("$..Errors[*]")).size()>0 ){
            JSONArray error = ctx.read("$..Errors[*]");
            for (Object er : error) {
                Map<String, Object> map = (Map<String, Object>) ((JSONArray) er).get(0);
                throw new FarandulaException(ErrorType.ACCESS_ERROR, map.get("ShortText").toString());

            }
        }

        if ( ctx.read( "$..errorCode") != null && ((JSONArray)ctx.read("$..errorCode")).size()>0  ){
            JSONArray error = ctx.read("$..errorCode");
            throw new FarandulaException(ErrorType.ACCESS_ERROR,  error.get(0).toString() );
        }
    }

    private Fares extractFaresInfo(JSONArray airItineraryPricingInfo) {

        Fares faresItinerary = new Fares();
        Price price = new Price(Double.parseDouble(getValueOf(airItineraryPricingInfo.get(0), "ItinTotalFare.BaseFare.Amount", String.class)),
                getValueOf(airItineraryPricingInfo.get(0), "ItinTotalFare.BaseFare.CurrencyCode", String.class));
        faresItinerary.setBasePrice(price);

        JSONArray taxes = (JSONArray) getValueOf(airItineraryPricingInfo.get(0), "ItinTotalFare.Taxes.Tax");
        price = new Price(Double.parseDouble(((Map<String, Object>) taxes.get(0)).get("Amount").toString()),
                ((Map<String, Object>) taxes.get(0)).get("Amount").toString());
        faresItinerary.setTaxesPrice(price);
        price = new Price(Double.parseDouble(getValueOf(airItineraryPricingInfo.get(0), "ItinTotalFare.TotalFare.Amount", String.class)),
                getValueOf(airItineraryPricingInfo.get(0), "ItinTotalFare.TotalFare.CurrencyCode", String.class));
        faresItinerary.setTotalPrice(price);

        return faresItinerary;

    }

    private void getSeats(AirLeg leg, ArrayList<List<Map>> cabinsBySegment) {

        int cabinIndex = 0;
        for (Segment segment : leg.getSegments()) {

            Map cabinsBySegmentMap = cabinsBySegment.get(0).get(cabinIndex);
            int numberOfSeats = getValueOf(cabinsBySegmentMap, "SeatsRemaining.Number", Integer.class);
            String cabinValue = getValueOf(cabinsBySegmentMap, "Cabin.Cabin", String.class);
            ;
            CabinClassType classType = getCabinClassType(convertCodeToTravelClass(cabinValue));

            List<Seat> seatsResult = new ArrayList<>();
            for (int i = 0; i < numberOfSeats; i++) {
                Seat seat = new Seat();
                seat.setClassCabin(classType);
                //sabre does not have the seat key
                seat.setPlace("");
                seatsResult.add(seat);
            }
            segment.setSeatsAvailable(seatsResult);
            cabinIndex++;
        }


    }

    private String convertCodeToTravelClass(String s) {

        if (codeToClassMap.containsKey(s)) {
            return codeToClassMap.get(s);
        }

        return "Other";
    }

    private ArrayList<List<Map>> extractCabinsInfo(JSONArray airItineraryPricingInfoMap) {

        ArrayList<List<Map>> cabinsBySegment = airItineraryPricingInfoMap
                .stream()
                .map(itineraryPricing -> {

                    JSONArray fareInfosArray = getValueOf(itineraryPricing, "FareInfos.FareInfo", JSONArray.class);
                    List<Map> cabins = fareInfosArray
                            .stream()
                            .map(fareInfo -> {
                                return getValueOf(fareInfo, "TPA_Extensions", Map.class);
                            })
                            .collect(Collectors.toCollection(ArrayList::new));

                    return cabins;
                }).collect(Collectors.toCollection(ArrayList::new));

        return cabinsBySegment;
    }

    private Itinerary buildAirLegs(Map<String, Object> pricedItinerary, SearchCommand searchCommand) {

        Map<String, Object> airItinerary = (Map<String, Object>) getValueOf(pricedItinerary, "AirItinerary");
        Map<String, Object> originDestinationOptions = (Map<String, Object>) getValueOf(airItinerary, "OriginDestinationOptions");
        JSONArray originDestinationOption = (JSONArray) getValueOf(originDestinationOptions, "OriginDestinationOption");

        Itinerary itinerary = new Itinerary();

        for (int i = 0; i < originDestinationOption.size(); i++) {

            Object option = originDestinationOption.get(i);
            JSONArray jsonSegmentArray = (JSONArray) getValueOf(option, "FlightSegment");

            List<Segment> segments = new ArrayList<>();
            for (int j = 0; j < jsonSegmentArray.size(); j++) {
                segments.add(buildSegment((Map<String, Object>) jsonSegmentArray.get(j)));
            }

            AirLeg leg = new AirLeg();
            leg.setId("tempID");
            leg.setDepartureAirportCode(segments.get(0).getDepartureAirportCode());
            leg.setDepartingDate(segments.get(0).getDepartureDate());
            leg.setArrivalAirportCode(segments.get(segments.size() - 1).getArrivalAirportCode());
            leg.setArrivalDate(segments.get(segments.size() - 1).getArrivalDate());
            leg.setSegments(segments);

            itinerary.getAirlegs().add( leg );

        }

        return itinerary;

    }

    private Segment buildSegment(Map<String, Object> g) {

        Map<String, Object> segmentMap = g;

        //airline
        JSONArray jsonEquipmentArray = (JSONArray) getValueOf(segmentMap, "Equipment");
        Map<String, Object> equipmentData = (Map<String, Object>) jsonEquipmentArray.get(0);
        Map<String, Object> operativeAirlineData = (Map<String, Object>) getValueOf(segmentMap, "OperatingAirline");
        Map<String, Object> marketingAirlineData = (Map<String, Object>) getValueOf(segmentMap, "MarketingAirline");

        //departure
        Map<String, Object> departureAirportData = (Map<String, Object>) getValueOf(segmentMap, "DepartureAirport");
        Map<String, Object> departureTimeZone = (Map<String, Object>) getValueOf(segmentMap, "DepartureTimeZone");
        //arrival
        Map<String, Object> arrivalAirportData = (Map<String, Object>) getValueOf(segmentMap, "ArrivalAirport");
        Map<String, Object> arrivalTimeZone = (Map<String, Object>) getValueOf(segmentMap, "ArrivalTimeZone");

        //Airleg data
        Segment seg = new Segment();
        seg.setOperatingAirlineCode((String) getValueOf(operativeAirlineData, "Code"));
        seg.setOperatingAirlineName(airlinesCodeMap.get(seg.getOperatingAirlineCode()));
        seg.setOperatingFlightNumber(operativeAirlineData.get("FlightNumber").toString());

        seg.setMarketingAirlineCode((String) getValueOf(marketingAirlineData, "Code"));
        seg.setMarketingAirlineName(airlinesCodeMap.get(seg.getMarketingAirlineCode()));
        // marketing flight number does not exist in sabre.
        seg.setMarketingFlightNumber("");

        seg.setAirplaneData((String) getValueOf(equipmentData, "AirEquipType"));
        //departure info
        seg.setDepartureAirportCode((String) getValueOf(departureAirportData, "LocationCode"));
        seg.setDepartureTerminal((String) getValueOf(departureAirportData, "TerminalID"));
        LocalDateTime departureDateTime = LocalDateTime.parse(
                (String) getValueOf(segmentMap, "DepartureDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setDepartureDate(departureDateTime);

        //arrival info
        seg.setArrivalAirportCode((String) getValueOf(arrivalAirportData, "LocationCode"));
        seg.setArrivalTerminal((String) getValueOf(arrivalAirportData, "TerminalID"));
        LocalDateTime arrivalDateTime = LocalDateTime.parse(
                (String) getValueOf(segmentMap, "ArrivalDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setArrivalDate(arrivalDateTime);

        //to obtain the Airleg time of this segment.
        long diffInMinutes = 0;
        if (getValueOf(departureTimeZone, "GMTOffset").equals(getValueOf(arrivalTimeZone, "GMTOffset"))) {
            diffInMinutes = Duration.between(departureDateTime, arrivalDateTime)
                    .toMinutes();
        } else {
            String GMT_ZONE_departure = getValueOf(departureTimeZone, "GMTOffset").toString();
            String GMT_ZONE_arrival = getValueOf(arrivalTimeZone, "GMTOffset").toString();
            Instant timeStampDeparture = departureDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter(GMT_ZONE_departure)));
            Instant timeStampArrival = arrivalDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter(GMT_ZONE_arrival)));
            diffInMinutes = Duration.between(timeStampDeparture, timeStampArrival).toMinutes();
        }
        seg.setDuration(diffInMinutes);

        return seg;
    }

}
