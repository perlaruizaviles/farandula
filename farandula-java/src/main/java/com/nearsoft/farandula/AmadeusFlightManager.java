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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
        JSONArray pricedItineraries = ctx.read("$..results[*]");
        return  pricedItineraries
                .stream()
                .map( f ->  {
                    Flight currentFly = new Flight();
                    currentFly.setLegs(buildAirLegs( (Map<String, Object>) f) );
                    //TODO change this PNR
                    currentFly.setPNR("tempPNR");
                    currentFly.setId( ((Map<String, Object>) f).get("SequenceNumber").toString()  );
                    return currentFly;

                }).collect(Collectors.toCollection( LinkedList::new ) );

    }

    private List<Airleg> buildAirLegs(Map<String, Object> pricedItinerary) {

        Map<String, Object> airItinerary = (Map<String, Object>) pricedItinerary.get("AirItinerary");
        Map<String, Object> originDestinationOptions = (Map<String, Object>) airItinerary.get("OriginDestinationOptions");
        JSONArray originDestinationOption = (JSONArray) originDestinationOptions.get("OriginDestinationOption");

        return originDestinationOption
                .stream()
                .map( option -> {
                    JSONArray jsonSegmentArray = (JSONArray) ((Map<String, Object>) option).get("FlightSegment");
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

    private Segment buildSegment(Map<String, Object> g) {
        Map<String, Object > segmentMap = g;
        //Todo change segment id and PATH
        //airline
        JSONArray jsonEquipmentArray = (JSONArray) segmentMap.get("Equipment");
        Map<String, Object > equipmentData = (Map<String, Object>) jsonEquipmentArray.get(0);
        Map<String, Object > airlineData = (Map<String, Object>) segmentMap.get("OperatingAirline");
        //departure
        Map<String, Object > departureAirportData = (Map<String, Object>) segmentMap.get("DepartureAirport");
        Map<String, Object > departureTimeZone = (Map<String, Object>) segmentMap.get("DepartureTimeZone");
        //arrival
        Map<String, Object > arrivalAirportData = (Map<String, Object>) segmentMap.get("ArrivalAirport");
        Map<String, Object > arrivalTimeZone = (Map<String, Object>) segmentMap.get("ArrivalTimeZone");

        Segment seg = new Segment();
        seg.setAirlineIconPath("");
        seg.setAirlineName( (String)airlineData.get("Code")  );
        seg.setFlightNumber( (String)segmentMap.get("FlightNumber"));
        seg.setDepartureAirportCode( (String)departureAirportData.get("LocationCode") );
        LocalDateTime departureDateTime = LocalDateTime.parse (
                (String)segmentMap.get("DepartureDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setDepartingDate( departureDateTime );
        seg.setArrivalAirportCode( (String)arrivalAirportData.get("LocationCode") );
        LocalDateTime arrivalDateTime = LocalDateTime.parse (
                (String)segmentMap.get("ArrivalDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setArrivalDate( arrivalDateTime );
        seg.setAirplaneData( (String) equipmentData.get("AirEquipType") );

        //to obtain the flight time of this segment.
        long diffInHours = 0;
        long diffInMinutes = 0;
        String timeFlight = "";
        if ( departureTimeZone.get("GMTOffset").equals( arrivalTimeZone.get("GMTOffset") ) ){
            diffInHours = Duration.between(departureDateTime, arrivalDateTime)
                    .toHours();
            diffInMinutes = Duration.between( departureDateTime, arrivalDateTime )
                    .toMinutes();
            timeFlight = diffInHours +  " h " + (diffInMinutes - (60 * diffInHours)) + " m.";

        }else{
            String GMT_ZONE_departure = departureTimeZone.get("GMTOffset").toString();
            String GMT_ZONE_arrival = arrivalTimeZone.get("GMTOffset").toString();
            Instant timeStampDeparture = departureDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter( GMT_ZONE_departure )) );
            Instant timeStampArrival = arrivalDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter( GMT_ZONE_arrival )) );
            diffInHours = Duration.between( timeStampDeparture, timeStampArrival ).toHours();
            diffInMinutes = Duration.between( timeStampDeparture, timeStampArrival ).toMinutes();
            timeFlight = diffInHours +  " h " + (diffInMinutes - (60 * diffInHours)) + " m.";
        }
        seg.setTimeFlight( timeFlight );

        return seg;
    }

    public Request buildRequestForAvail(SearchCommand search) {

        final Request.Builder builder = new Request.Builder();
        String departureDate = search.getDepartingDate().format( DateTimeFormatter.ISO_LOCAL_DATE );
        String arrivalDate = search.getReturningDate().format( DateTimeFormatter.ISO_LOCAL_DATE );
        String api = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";
        String url_api = api +
                "apikey=" +  apiKey
                + "&origin=" + search.getDepartureAirport()
                + "&destination=" + search.getArrivalAirport()
                + "&departure_date=" + departureDate
                + "&return_date=" + arrivalDate
                + "&adults=" + search.getPassengers().size()
                + "&number_of_results=" + search.getOffSet() ;
        builder.url( url_api);
        builder.get();
        return builder.build() ;
    }


    InputStream sendRequest(Request request) throws IOException, FarandulaException {
        final Response response = buildHttpClient().newCall( request).execute();
        return response.body().byteStream();
    }


}
