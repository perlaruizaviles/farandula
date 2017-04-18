package com.nearsoft.farandula;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

//TODO consider create an specic trip manager for each API or create a connector/plugin framework
public class TripManager {

    //TODO should we use an HTTP client lib or its better to do it bare bones  (ProofOfConcept) pros and cons?
    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();
    private final AccessManager _accessManager;

    public TripManager(Creds creds) {
        _accessManager = new AccessManager(creds);
    }

    public List<Flight> getAvail(SearchCommand search) throws FarandulaException {

        try {
            //TODO implement a mock client that returns a hardcoded response, for faster dev cycles.
            final Response response = buildHttpClient().newCall(buildRequestForAvail( search )).execute();
            return buildAvailResponse(response.body().byteStream());

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    List<Flight> buildAvailResponse(InputStream response) throws IOException {

        List<Flight> resultFlights = new LinkedList<>();
        ReadContext ctx = JsonPath.parse(response);
        JSONArray pricedItineraries = ctx.read("$..PricedItinerary[*]");
        //Todo we should find a better functional way to construct the result flight( map/collect )
        pricedItineraries.forEach( f ->  {
            Flight currentFly = new Flight();
            currentFly.setLegs(buildAirLegs( (Map<String, Object>) f) );
            //TODO change this PNR and ID
            currentFly.setPNR("tempPNR");
            currentFly.setId("tempID");
            resultFlights.add(currentFly);

        });

        return resultFlights;
    }

    private List<Airleg> buildAirLegs( Map<String, Object> pricedItinerary) {

        Map<String, Object> airItinerary = (Map<String, Object>) pricedItinerary.get("AirItinerary");
        Map<String, Object> originDestinationOptions = (Map<String, Object>) airItinerary.get("OriginDestinationOptions");
        JSONArray originDestinationOption = (JSONArray) originDestinationOptions.get("OriginDestinationOption");

        List<Airleg> airlegList = new LinkedList<>();
        originDestinationOption.forEach( option ->  {

            JSONArray jsonSegmentArray = (JSONArray) ( (Map<String, Object>) option).get("FlightSegment");
            List<Segment> segments = new LinkedList<>() ;
            //Todo we should find a better functional way to contruct the result flight( map/collect )
            jsonSegmentArray.forEach( g -> {
                segments.add( buildSegment( (Map<String, Object>) g) );
            });

            if ( !segments.isEmpty() ) {
                Airleg leg = new Airleg();
                leg.setId("tempID");
                leg.setDepartureAirportCode( segments.get(0).getDepartureAirportCode() );
                leg.setDepartingDate( segments.get(0).getDepartingDate() );
                leg.setArrivalAirportCode( segments.get(segments.size() - 1).getArrivalAirportCode() );
                leg.setArrivalDate( segments.get(segments.size() - 1).getArrivalDate() );
                leg.setSegments( segments );
                airlegList.add( leg );
            }

        } );

        return airlegList;


    }

    private Segment buildSegment( Map<String, Object> g) {
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
        seg.setId("");
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


    String buildJsonFromSearch(SearchCommand search) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //SabreJSONRequest.getRequest helps to create the JSON request depending of the search object
        JsonNode rootNode = mapper.readTree( SabreJSONRequest.getRequest( search ) );

        return rootNode.toString();

    }

    private OkHttpClient buildHttpClient() throws FarandulaException {
        if (_builder.interceptors().isEmpty()) {
            _builder.addInterceptor(new AuthInterceptor(_accessManager.getAccessToken()));
            _builder.connectTimeout(1, TimeUnit.MINUTES);
            _builder.readTimeout(1, TimeUnit.MINUTES);
        }
        return _builder.build();
    }

    public static TripManager sabre() throws IOException, FarandulaException {

        Properties props = new Properties();
        props.load(TripManager.class.getResourceAsStream("/config.properties"));
        final Creds creds = new Creds(props.getProperty("sabre.client_id"), props.getProperty("sabre.client_secret"));
        TripManager tripManager = new TripManager(creds);
        return tripManager;

    }

    public List<Flight> executeAvail(SearchCommand searchCommand) throws FarandulaException {

        return this.getAvail(searchCommand);

    }
}