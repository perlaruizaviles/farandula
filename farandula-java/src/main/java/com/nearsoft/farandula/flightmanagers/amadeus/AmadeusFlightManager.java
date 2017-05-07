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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 4/20/17.
 */
public class AmadeusFlightManager implements FlightManager {

    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();

    private static String apiKey;

    private static Map<String, String> locationsMap = new HashMap<>();


    static {
        Properties props = new Properties();
        try {
            props.load(AmadeusFlightManager.class.getResourceAsStream("/config.properties"));
            fillLocationsMap();
            apiKey = props.getProperty("amadeus.apikey");
        } catch (IOException e) {
            e.printStackTrace();
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
    public List<AirLeg> getAvail(SearchCommand search) throws FarandulaException {

        try {
            Request request = buildRequestForAvail(search);
            InputStream responseStream = sendRequest(request);
            return parseAvailResponse(responseStream);

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    public static void fillLocationsMap() throws IOException {
        Properties properties = new Properties();
        properties.load(AmadeusFlightManager.class.getResourceAsStream("/amadeus/locations.properties"));
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            locationsMap.put(key, value);
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


    public List<AirLeg> parseAvailResponse(InputStream response) throws IOException {

        ReadContext ctx = JsonPath.parse(response);
        JSONArray pricedItineraries = ctx.read("$..results[*].itineraries[*]");

        List<AirLeg> legs = new ArrayList<>();
        for (Object pricedItinerary : pricedItineraries) {
            legs.addAll(buildAirLegs((Map<String, Object>) pricedItinerary));
        }
        return legs;

    }

    private List<AirLeg> buildAirLegs(Map<String, Object> itinerary) {

        List<AirLeg> results = new LinkedList<>();

        //adds departure leg
        Map<String, Object> outbound = (Map<String, Object>) itinerary.get("outbound");
        JSONArray outboundFlights = (JSONArray) outbound.get("flights");
        results.add(getAirleg(outboundFlights));

        //adds arrival leg
        Map<String, Object> inbound = (Map<String, Object>) itinerary.get("inbound");
        JSONArray inboundFlights = (JSONArray) inbound.get("flights");
        results.add(getAirleg(inboundFlights));

        return results;

    }

    private AirLeg getAirleg(JSONArray outboundFlights) {
        LinkedList<Segment> segmentsOtbound = outboundFlights
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
        leg.setDepartureAirportCode(segmentsOtbound.get(0).getDepartureAirportCode());
        leg.setDepartingDate(segmentsOtbound.get(0).getDepartureDate());
        leg.setArrivalAirportCode(segmentsOtbound.get(segmentsOtbound.size() - 1).getArrivalAirportCode());
        leg.setArrivalDate(segmentsOtbound.get(segmentsOtbound.size() - 1).getArrivalDate());
        leg.setSegments(segmentsOtbound);
        return leg;
    }

    private Segment buildSegment(Map<String, Object> segmentMap) throws IOException, FarandulaException {

        //departure
        Map<String, Object> departureAirportData = (Map<String, Object>) segmentMap.get("origin");

        //arrival
        Map<String, Object> arrivalAirportData = (Map<String, Object>) segmentMap.get("destination");

        //booking_info
        Map<String, Object> bookingInfoData = (Map<String, Object>) segmentMap.get("booking_info");

        //Airleg information
        Segment seg = new Segment();
        seg.setAirlineIconPath("");
        seg.setOperatingAirlineCode((String) segmentMap.get("operative_airline"));
        seg.setMarketingAirlineCode((String) segmentMap.get("marketing_airline"));
        seg.setMarketingFlightNumber((String) segmentMap.get("flight_number"));
        seg.setAirplaneData((String) segmentMap.get("aircraft"));
        seg.setTravelClass((String) bookingInfoData.get("travel_class"));

        //departure stuff
        seg.setDepartureAirportCode((String) departureAirportData.get("airport"));
        seg.setDepartureTerminal((String) departureAirportData.get("terminal"));
        LocalDateTime departureDateTime = LocalDateTime.parse(
                (String) segmentMap.get("departs_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setDepartureDate(departureDateTime);

        //arrival stuff
        seg.setArrivalAirportCode((String) arrivalAirportData.get("airport"));
        seg.setDepartureTerminal((String) arrivalAirportData.get("terminal"));
        LocalDateTime arrivalDateTime = LocalDateTime.parse(
                (String) segmentMap.get("arrives_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setArrivalDate(arrivalDateTime);

        //TODO CHECK this block is to improve the performance.
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
        String departureDate = search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String arrivalDate = search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String apiURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";

        apiURL +="apikey=" + apiKey
                + "&origin=" + search.getDepartureAirport()
                + "&destination=" + search.getArrivalAirport()
                + "&departure_date=" + departureDate;

        if ( search.getType() == FlightType.ROUNDTRIP) {
            apiURL += "&return_date=" + arrivalDate;
        }

        apiURL += "&adults=" + search.getPassengers().size()
                + "&number_of_results=" + search.getOffSet();


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
