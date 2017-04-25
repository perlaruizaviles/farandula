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
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO consider create an specific trip manager for each API or create a connector/plugin framework
public class SabreTripFlightManager implements FlightManager {

    //TODO should we use an HTTP client lib or its better to do it bare bones  (ProofOfConcept) pros and cons?
    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();
    private final AccessManager _accessManager;

    public SabreTripFlightManager(Creds creds) {
        _accessManager = new AccessManager(creds);
    }

    public static SabreTripFlightManager prepareSabre() throws IOException, FarandulaException {

        Properties props = new Properties();
        props.load(SabreTripFlightManager.class.getResourceAsStream("/config.properties"));
        final Creds creds = new Creds(props.getProperty("sabre.client_id"), props.getProperty("sabre.client_secret"));
        SabreTripFlightManager tripManager = new SabreTripFlightManager(creds);
        return tripManager;

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

    Stream<Flight> parseAvailResponse(InputStream response) throws IOException {

        ReadContext ctx = JsonPath.parse(response);
        JSONArray pricedItineraries = ctx.read("$..PricedItinerary[*]");
        Stream<Flight> flightStream = pricedItineraries
                .stream()
                .map(f -> {
                    Flight currentFly = new Flight();
                    currentFly.setLegs(buildAirLegs((Map<String, Object>) f));
                    currentFly.setId(((Map<String, Object>) f).get("SequenceNumber").toString());
                    return currentFly;

                });
        return  flightStream;

    }

    private List<Airleg> buildAirLegs( Map<String, Object> pricedItinerary) {

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

    private Segment buildSegment( Map<String, Object> g) {
        Map<String, Object > segmentMap = g;
        //Todo change segment id and PATH
        //airline
        JSONArray jsonEquipmentArray = (JSONArray) segmentMap.get("Equipment");
        Map<String, Object > equipmentData = (Map<String, Object>) jsonEquipmentArray.get(0);
        Map<String, Object > operativeAirlineData = (Map<String, Object>) segmentMap.get("OperatingAirline");
        Map<String, Object > marketingAirlineData = (Map<String, Object>) segmentMap.get("MarketingAirline");

        //departure
        Map<String, Object > departureAirportData = (Map<String, Object>) segmentMap.get("DepartureAirport");
        Map<String, Object > departureTimeZone = (Map<String, Object>) segmentMap.get("DepartureTimeZone");
        //arrival
        Map<String, Object > arrivalAirportData = (Map<String, Object>) segmentMap.get("ArrivalAirport");
        Map<String, Object > arrivalTimeZone = (Map<String, Object>) segmentMap.get("ArrivalTimeZone");

        //flight data
        Segment seg = new Segment();
        seg.setAirlineIconPath("");
        seg.setOperatingAirline( (String)operativeAirlineData.get("Code")  );
        seg.setMarketingAirline( (String)marketingAirlineData.get("Code")  );
        seg.setFlightNumber( (String)segmentMap.get("FlightNumber"));
        seg.setAirplaneData( (String) equipmentData.get("AirEquipType") );
        //TODO travel class for SABRE
        seg.setTravelClass("");

        //departure info
        seg.setDepartureAirportCode( (String)departureAirportData.get("LocationCode") );
        seg.setDepartureTerminal( (String)departureAirportData.get("TerminalID" ) );
        LocalDateTime departureDateTime = LocalDateTime.parse (
                (String)segmentMap.get("DepartureDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setDepartingDate( departureDateTime );

        //arrival info
        seg.setArrivalAirportCode( (String)arrivalAirportData.get("LocationCode") );
        seg.setArrivalTerminal( (String) arrivalAirportData.get("TerminalID") );
        LocalDateTime arrivalDateTime = LocalDateTime.parse (
                (String)segmentMap.get("ArrivalDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME );
        seg.setArrivalDate( arrivalDateTime );

        //to obtain the flight time of this segment.
        long diffInMinutes = 0;
        if ( departureTimeZone.get("GMTOffset").equals( arrivalTimeZone.get("GMTOffset") ) ){
            diffInMinutes = Duration.between( departureDateTime, arrivalDateTime )
                    .toMinutes();
        }else{
            String GMT_ZONE_departure = departureTimeZone.get("GMTOffset").toString();
            String GMT_ZONE_arrival = arrivalTimeZone.get("GMTOffset").toString();
            Instant timeStampDeparture = departureDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter( GMT_ZONE_departure )) );
            Instant timeStampArrival = arrivalDateTime.toInstant(
                    ZoneOffset.of(GMTFormatter.GMTformatter( GMT_ZONE_arrival )) );
            diffInMinutes = Duration.between( timeStampDeparture, timeStampArrival ).toMinutes();
        }
        seg.setDuration( diffInMinutes );

        return seg;
    }

    String buildJsonFromSearch(SearchCommand search) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //SabreJSONRequest.getRequest helps to create the JSON request depending of the search object
        JsonNode rootNode = mapper.readTree( SabreJSONRequest.getRequest( search ) );

        return rootNode.toString();

    }

}