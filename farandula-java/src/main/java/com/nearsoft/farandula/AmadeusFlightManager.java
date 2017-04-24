package com.nearsoft.farandula;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nearsoft.farandula.models.Airleg;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;
import com.nearsoft.farandula.models.Segment;
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

/**
 * Created by pruiz on 4/20/17.
 */
public class AmadeusFlightManager implements FlightManager {

    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();

    private String apiKey;

    public AmadeusFlightManager() throws IOException, FarandulaException {

        Properties props = new Properties();
        props.load(this.getClass().getResourceAsStream("/config.properties"));
        apiKey = props.getProperty("amadeus.apikey") ;

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
            Request request = buildRequestForAvail( search );
            InputStream responseStream = sendRequest(request);
            return buildAvailResponse(responseStream);

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    public List<Flight> buildAvailResponse(InputStream response) throws IOException {

        ReadContext ctx = JsonPath.parse(response);
        JSONArray pricedItineraries = ctx.read("$..results[*].itineraries[*]");
        return  pricedItineraries
                .stream()
                .map( f ->  {
                    Flight currentFly = new Flight();
                    currentFly.setLegs( buildAirLegs( (Map<String, Object>) f) );
                    //TODO change this PNR
                    currentFly.setPNR("tempPNR");
                    //TODO how to set the ID for amadeus??
                    return currentFly;

                }).collect(Collectors.toCollection( LinkedList::new ) );

    }

    private List<Airleg> buildAirLegs(Map<String, Object> itinerary) {

        List<Airleg> results =  new LinkedList<>();

        //adds departure leg
        Map<String, Object> outbound = (Map<String, Object>) itinerary.get("outbound");
        JSONArray outboundFlights = (JSONArray) outbound.get("flights");
        results.add( getAirleg(outboundFlights) );

        //adds arrival leg
        Map<String, Object> inbound = (Map<String, Object>) itinerary.get("inbound");
        JSONArray inboundFlights = (JSONArray) inbound.get("flights");
        results.add( getAirleg( inboundFlights) );

        return results;

    }

    private Airleg getAirleg(JSONArray outboundFlights) {
        LinkedList<Segment> segmentsOtbound = outboundFlights
                .stream()
                .map(segment -> {
                    return buildSegment((Map<String, Object>) segment);
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

    private Segment buildSegment(Map<String, Object> segmentMap) {

        //departure
        Map<String, Object > departureAirportData = (Map<String, Object>) segmentMap.get("origin");
        //Map<String, Object > departureTimeZone = (Map<String, Object>) segmentMap.get("DepartureTimeZone");

        //arrival
        Map<String, Object > arrivalAirportData = (Map<String, Object>) segmentMap.get("destination");
        //Map<String, Object > arrivalTimeZone = (Map<String, Object>) segmentMap.get("ArrivalTimeZone");

        Segment seg = new Segment();
        seg.setAirlineIconPath("");
        seg.setAirlineName(  (String)segmentMap.get("marketing_airline")  );
        //also there is an operative airline
        seg.setFlightNumber( (String)segmentMap.get("flight_number"));
        seg.setDepartureAirportCode( (String)departureAirportData.get("airport") );
        //also there is terminal
        LocalDateTime departureDateTime = LocalDateTime.parse (
                (String)segmentMap.get("departs_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setDepartingDate( departureDateTime );

        seg.setArrivalAirportCode( (String)arrivalAirportData.get("airport") );
        LocalDateTime arrivalDateTime = LocalDateTime.parse (
                (String)segmentMap.get("arrives_at"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setArrivalDate( arrivalDateTime );
        seg.setAirplaneData( (String) segmentMap.get("aircraft") );

        //to obtain the flight time of this segment.
        //TODO amadeus doesn't provide GMT zone
        long diffInHours = Duration.between(departureDateTime, arrivalDateTime).toHours();
        long diffInMinutes = Duration.between( departureDateTime, arrivalDateTime ).toMinutes();
        String timeFlight = diffInHours +  " h " + (diffInMinutes - (60 * diffInHours)) + " m.";
        seg.setTimeFlight( timeFlight );

        //there is class information
        //"booking_info": {
        //    "travel_class": "ECONOMY"
        return seg;
    }

    public Request buildRequestForAvail(SearchCommand search) {

        final Request.Builder builder = new Request.Builder();
        String url_api = buildTargetURLFromSearch(search);
        builder.url( url_api);
        builder.get();
        return builder.build() ;
    }

    String buildTargetURLFromSearch(SearchCommand search) {
        String departureDate = search.getDepartingDate().format( DateTimeFormatter.ISO_LOCAL_DATE );
        String arrivalDate = search.getReturningDate().format( DateTimeFormatter.ISO_LOCAL_DATE );
        String api = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";
        return api +
                "apikey=" +  apiKey
                + "&origin=" + search.getDepartureAirport()
                + "&destination=" + search.getArrivalAirport()
                + "&departure_date=" + departureDate
                + "&return_date=" + arrivalDate
                + "&adults=" + search.getPassengers().size()
                + "&number_of_results=" + search.getOffSet();
    }


    InputStream sendRequest(Request request) throws IOException, FarandulaException {
        final Response response = buildHttpClient().newCall( request).execute();
        return response.body().byteStream();
    }

}
