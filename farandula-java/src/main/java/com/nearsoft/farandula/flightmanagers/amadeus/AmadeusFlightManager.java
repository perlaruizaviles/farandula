package com.nearsoft.farandula.flightmanagers.amadeus;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.Airleg;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;
import com.nearsoft.farandula.models.Segment;
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
import java.util.stream.Stream;

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
    public List<Flight> getAvail(SearchCommand search) throws FarandulaException {

        try {
            Request request = buildRequestForAvail(search);
            InputStream responseStream = sendRequest(request);
            Stream<Flight> flightStream = parseAvailResponse(responseStream);
            return flightStream.collect(Collectors.toCollection(LinkedList::new));

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


    public Stream<Flight> parseAvailResponse(InputStream response) throws IOException {

        ReadContext ctx = JsonPath.parse(response);
        JSONArray pricedItineraries = ctx.read("$..results[*].itineraries[*]");
        return pricedItineraries
                .stream()
                .map(f -> {
                    Flight currentFly = new Flight();
                    currentFly.setLegs(buildAirLegs((Map<String, Object>) f));
                    //TODO change this PNR
                    currentFly.setPNR("tempPNR");
                    //TODO how to set the ID for amadeus??
                    return currentFly;

                });

    }

    private List<Airleg> buildAirLegs(Map<String, Object> itinerary) {

        List<Airleg> results = new LinkedList<>();

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

    private Airleg getAirleg(JSONArray outboundFlights) {
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

        Airleg leg = new Airleg();
        leg.setId("tempID");
        leg.setDepartureAirportCode(segmentsOtbound.get(0).getDepartureAirportCode());
        leg.setDepartingDate(segmentsOtbound.get(0).getDepartingDate());
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

        //flight information
        Segment seg = new Segment();
        seg.setAirlineIconPath("");
        seg.setOperatingAirline((String) segmentMap.get("operative_airline"));
        seg.setMarketingAirline((String) segmentMap.get("marketing_airline"));
        seg.setFlightNumber((String) segmentMap.get("flight_number"));
        seg.setAirplaneData((String) segmentMap.get("aircraft"));
        seg.setTravelClass((String) bookingInfoData.get("travel_class"));

        //departure stuff
        seg.setDepartureAirportCode((String) departureAirportData.get("airport"));
        seg.setDepartureTerminal((String) departureAirportData.get("terminal"));
        LocalDateTime departureDateTime = LocalDateTime.parse(
                (String) segmentMap.get("departs_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setDepartingDate(departureDateTime);

        //arrival stuff
        seg.setArrivalAirportCode((String) arrivalAirportData.get("airport"));
        seg.setDepartureTerminal((String) arrivalAirportData.get("terminal"));
        LocalDateTime arrivalDateTime = LocalDateTime.parse(
                (String) segmentMap.get("arrives_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        seg.setArrivalDate(arrivalDateTime);

        //TODO CHECK this block is to improve the performance.
        String departureTimeZone = "";
        if ( locationsMap.containsKey(seg.getDepartureAirportCode()) ) {
            departureTimeZone = locationsMap.get(seg.getDepartureAirportCode());
        } else {
            departureTimeZone = getTimeZone(seg.getDepartureAirportCode());
            locationsMap.put(seg.getDepartureAirportCode(), departureTimeZone);
        }

        String arrivalTimeZone = "";
        if (locationsMap.containsKey( seg.getArrivalAirportCode()) ) {
            arrivalTimeZone = locationsMap.get(seg.getArrivalAirportCode());
        } else {
            arrivalTimeZone = getTimeZone(seg.getArrivalAirportCode());
            locationsMap.put(seg.getArrivalAirportCode(), arrivalTimeZone);
        }

        getTimeZone(seg.getArrivalAirportCode());
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
        String api = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";
        return api +
                "apikey=" + apiKey
                + "&origin=" + search.getDepartureAirport()
                + "&destination=" + search.getArrivalAirport()
                + "&departure_date=" + departureDate
                + "&return_date=" + arrivalDate
                + "&adults=" + search.getPassengers().size()
                + "&number_of_results=" + search.getOffSet();
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
