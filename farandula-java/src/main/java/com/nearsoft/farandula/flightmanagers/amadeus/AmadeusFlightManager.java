package com.nearsoft.farandula.flightmanagers.amadeus;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.*;
import net.minidev.json.JSONArray;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.nearsoft.farandula.utilities.CabinClassParser.getCabinClassType;
import static com.nearsoft.farandula.utilities.CurrencyIATACodesHelper.buildPrice;
import static com.nearsoft.farandula.utilities.NestedMapsHelper.getValueOf;

/**
 * Created by pruiz on 4/20/17.
 */
public class AmadeusFlightManager implements FlightManager {

    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();
    private static String apiKey;
    private static Map<String, String> locationsMap = new HashMap<>();
    private static Map<String,String> airlinesCodeMap = new HashMap<>();

    static {
        Properties props = new Properties();
        try {
            props.load(AmadeusFlightManager.class.getResourceAsStream("/config.properties"));
            fillReferenceMaps();
            apiKey = props.getProperty("amadeus.apikey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fillReferenceMaps() throws IOException {
        Properties properties = new Properties();
        properties.load(AmadeusFlightManager.class.getResourceAsStream("/amadeus/locations.properties"));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            locationsMap.put(key, value);
        }

        properties.clear();
        properties.load( AmadeusFlightManager.class.getResourceAsStream("/airlinesCode.properties"));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            airlinesCodeMap.put(key, value);
        }

    }

    private OkHttpClient buildHttpClient() {
        if (_builder.interceptors().isEmpty()) {
            _builder.connectTimeout(1, TimeUnit.MINUTES);
            _builder.readTimeout(1, TimeUnit.MINUTES);
        }
        return _builder.build();
    }

    @Override
    public List<Itinerary> getAvail(SearchCommand search) throws FarandulaException {

        try {
            Request request = buildRequestForAvail(search);
            InputStream responseStream = sendRequest(request);
            return parseAvailResponse(responseStream);

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    public String getTimeZone(String location_code) throws IOException, FarandulaException {

        String url_api = buildLocationURL(location_code);
        Request request = buildRequest(url_api);
        Response response = buildHttpClient().newCall(request).execute();
        InputStream responseStream = response.body().byteStream();
        ReadContext context = JsonPath.parse(responseStream);
        JSONArray airports = context.read("$..airports[*]");

        List<String> timezones = airports
                .stream()
                .filter(airport -> ((Map<String, Object>) airport).get("code").equals(location_code))
                .map(airport -> {
                    return (String) ((Map<String, Object>) airport).get("timezone");
                })
                .collect(Collectors.toCollection(ArrayList::new));

        return timezones.size() > 0 ? timezones.get(0) : "";

    }


    public List<Itinerary> parseAvailResponse(InputStream response) throws IOException {


        List<Itinerary> itineraries = new ArrayList<>();
        ReadContext ctx = JsonPath.parse(response);
        JSONArray results = ctx.read("$..results[*]");

        for ( Object result : results ){

            Itinerary itineraryResult = new Itinerary();

            Map<String, Object> resultMap = (Map<String, Object>) result;

            JSONArray arrayItineraries = (JSONArray) resultMap.get("itineraries");

            for ( Object itinerary : arrayItineraries ){

               buildAirLegs((Map<String, Object>) itinerary, itineraryResult );

            }

            //pricing
            Map<String, Object> fareMap = (Map<String, Object>) ((LinkedHashMap) result).get("fare");
            itineraryResult.setPrice( getPrices( fareMap ) );

            itineraries.add( itineraryResult );

        }

        return itineraries;

    }

    private void buildAirLegs(Map<String, Object> itineraryMap, Itinerary itineraryResult ) {

        //adds departure leg
        Map<String, Object> outbound = (Map<String, Object>) itineraryMap.get("outbound");
        JSONArray outboundFlights = (JSONArray) outbound.get("flights");
        AirLeg departureLeg = getAirleg(outboundFlights);
        itineraryResult.getAirlegs().add( departureLeg );

        //adds returnings leg
        Map<String, Object> inbound = (Map<String, Object>) itineraryMap.get("inbound");
        JSONArray inboundFlights = (JSONArray) inbound.get("flights");
        AirLeg returnigLeg= getAirleg(inboundFlights);
        itineraryResult.getAirlegs().add(returnigLeg );

    }

    private Fares getPrices(Map<String, Object> pricingInfoData) {

        //price
        Fares fares =  new Fares();
        Price totalPrice = buildPrice( getValueOf( pricingInfoData , "total_price", String.class ) );
        fares.setTotalPrice( totalPrice );

        if ( pricingInfoData.get("price_per_adult") != null  ) {
            fares.setPricePerAdult( buildPrice( getValueOf(pricingInfoData, "price_per_adult.total_fare", String.class)));
            fares.setTaxPerAdult( buildPrice( getValueOf(pricingInfoData, "price_per_adult.tax", String.class) ) );
        }

        if ( pricingInfoData.get("price_per_child") != null  ) {
            fares.setPricePerChild( buildPrice(getValueOf(pricingInfoData, "price_per_child.total_fare", String.class)));
            fares.setTaxPerChild( buildPrice(getValueOf(pricingInfoData, "price_per_child.tax", String.class)));
        }

        if ( pricingInfoData.get("price_per_infant") != null  ) {
            fares.setPricePerInfant(buildPrice(getValueOf(pricingInfoData, "price_per_infant.total_fare", String.class)));
            fares.setTaxPerInfant(buildPrice(getValueOf(pricingInfoData, "price_per_infant.tax", String.class)));
        }

        return fares;
    }

    private AirLeg getAirleg(JSONArray outboundFlights) {
        LinkedList<Segment> segmentsOutbound = outboundFlights
                .stream()
                .map(segment -> {
                    try {
                        return buildSegment((Map<String, Object>) segment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (FarandulaException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toCollection(LinkedList::new));

        AirLeg leg = new AirLeg();
        leg.setId("tempID");
        leg.setDepartureAirportCode(segmentsOutbound.get(0).getDepartureAirportCode());
        leg.setDepartingDate(segmentsOutbound.get(0).getDepartureDate());
        leg.setArrivalAirportCode(segmentsOutbound.get(segmentsOutbound.size() - 1).getArrivalAirportCode());
        leg.setArrivalDate(segmentsOutbound.get(segmentsOutbound.size() - 1).getArrivalDate());
        leg.setSegments(segmentsOutbound);
        return leg;
    }

    private Segment buildSegment(Map<String, Object> segmentMap) throws IOException, FarandulaException {

        //departure
        Map<String, Object> departureAirportData = (Map<String, Object>) segmentMap.get("origin");

        //arrival
        Map<String, Object> arrivalAirportData = (Map<String, Object>) segmentMap.get("destination");

        //booking_info
        Map<String, Object> bookingInfoData = (Map<String, Object>) segmentMap.get("booking_info");

        //pricing_info
        Map<String, Object> pricingInfoData = (Map<String, Object>) segmentMap.get("fare");

        //Airleg information
        Segment seg = new Segment();
        seg.setAirlineIconPath("");
        seg.setOperatingAirlineCode((String) segmentMap.get("operating_airline"));
        seg.setOperatingAirlineName( airlinesCodeMap.get( seg.getOperatingAirlineCode() ) );
        seg.setOperatingFlightNumber( (String) segmentMap.get("flight_number")  );

        seg.setMarketingAirlineCode((String) segmentMap.get("marketing_airline"));
        seg.setMarketingAirlineName( airlinesCodeMap.get( seg.getMarketingAirlineCode() ) );
        seg.setMarketingFlightNumber((String) segmentMap.get("flight_number"));

        seg.setAirplaneData((String) segmentMap.get("aircraft"));

        CabinClassType classTravel =  getCabinClassType( (String) bookingInfoData.get("travel_class") );
        int numberOfSeats = (int) bookingInfoData.get("seats_remaining");
        List<Seat> seats = new ArrayList<>();
        for ( int i = 0 ; i < numberOfSeats ; i++ ){
            Seat seat = new Seat();
            //amadeus does not indicate the seat place
            seat.setPlace("");
            seat.setClassCabin( classTravel );
            seats.add( seat );
        }
        seg.setSeatsAvailable(seats);

        //departure stuff
        seg.setDepartureAirportCode((String) departureAirportData.get("airport"));
        seg.setDepartureTerminal( (String) departureAirportData.get("terminal")) ;
        LocalDateTime departureDateTime = LocalDateTime.parse(
                (String) segmentMap.get("departs_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setDepartureDate(departureDateTime);

        //arrival stuff
        seg.setArrivalAirportCode((String) arrivalAirportData.get("airport"));
        seg.setArrivalTerminal( (String) arrivalAirportData.get("terminal") );
        LocalDateTime arrivalDateTime = LocalDateTime.parse(
                (String) segmentMap.get("arrives_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setArrivalDate(arrivalDateTime);

        String departureTimeZone = "";
        if (locationsMap.containsKey(seg.getDepartureAirportCode())) {
            departureTimeZone = locationsMap.get(seg.getDepartureAirportCode());
        } else {
            departureTimeZone = getTimeZone(seg.getDepartureAirportCode());
            locationsMap.put(seg.getDepartureAirportCode(), departureTimeZone);
        }

        String arrivalTimeZone = "";
        if (locationsMap.containsKey(seg.getArrivalAirportCode())) {
            arrivalTimeZone = locationsMap.get(seg.getArrivalAirportCode());
        } else {
            arrivalTimeZone = getTimeZone(seg.getArrivalAirportCode());
            locationsMap.put(seg.getArrivalAirportCode(), arrivalTimeZone);
        }

        long diffInMinutes = 0;
        if (departureTimeZone.equals(arrivalTimeZone)) {
            diffInMinutes = Duration.between(departureDateTime, arrivalDateTime).toMinutes();
        } else {
            if (departureTimeZone.isEmpty() || arrivalTimeZone.isEmpty()) {
                //todo check this case, what should we do when is impossible to get the zone,
                // example when location is 'xyz'
                diffInMinutes = 0;
            } else {
                ZonedDateTime departureWithZone = departureDateTime.atZone(ZoneId.of(departureTimeZone));
                ZonedDateTime arrivalWithZone = arrivalDateTime.atZone(ZoneId.of(arrivalTimeZone));
                diffInMinutes = Duration.between(departureWithZone, arrivalWithZone).toMinutes();
            }
        }

        seg.setDuration(diffInMinutes);

        return seg;
    }

    public Request buildRequestForAvail(SearchCommand search) {

        String url_api = buildTargetURLFromSearch(search);
        return buildRequest(url_api);
    }

    public Request buildRequest(String url_api) {
        final Request.Builder builder = new Request.Builder();
        builder.url(url_api);
        builder.get();
        return builder.build();
    }

    public String buildTargetURLFromSearch(SearchCommand search) {

        List<String> departureDateList = new ArrayList<>();
        for(  LocalDateTime departing :  search.getDepartingDates() ){
            departureDateList.add( departing.format( DateTimeFormatter.ISO_LOCAL_DATE  ) );
        }



        String apiURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";

        apiURL +="apikey=" + apiKey;

        for ( int i = 0 ; i < search.getArrivalAirports().size() ; i++ ) {
            apiURL += "&origin=" + search.getDepartureAirports().get( i )
                    + "&destination=" + search.getArrivalAirports().get( i )
                    +"&departure_date=" + departureDateList.get(i);
        }

        if ( search.getType() == FlightType.ROUNDTRIP) {
            String arrivalDate = search.getReturningDates().get(0).format( DateTimeFormatter.ISO_LOCAL_DATE  ) ;
            apiURL += "&return_date=" + arrivalDate;
        }


        for ( Map.Entry< PassengerType, List<Passenger> > entry : search.getPassengersMap().entrySet()  ){
            apiURL += "&"+  entry.getKey().toString().toLowerCase() + "=" + entry.getValue().size();
        }

        apiURL += "&number_of_results=" + search.getOffSet();

        //cabins for amadeus.
        switch ( search.getCabinClass() ){

            case ECONOMY:
                apiURL+= "&travel_class=ECONOMY";
                break;

            case PREMIUM_ECONOMY:
                apiURL+= "&travel_class=PREMIUM_ECONOMY";
                break;

            case FIRST:
                apiURL+= "&travel_class=FIRST";
                break;

            case BUSINESS:
                apiURL+= "&travel_class=FIRST";
                break;

            default:
                apiURL+= "&travel_class=ECONOMY";
        }

        return apiURL;
    }

    String buildLocationURL(String location) {

        String api = "http://api.sandbox.amadeus.com/v1.2/location/"
                + location
                + "/?apikey=" + apiKey;
        return api;
    }

    public InputStream sendRequest(Request request) throws IOException, FarandulaException {
        final Response response = buildHttpClient().newCall(request).execute();
        return response.body().byteStream();
    }

}
