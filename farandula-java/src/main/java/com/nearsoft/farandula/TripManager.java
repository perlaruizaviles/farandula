package com.nearsoft.farandula;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nearsoft.farandula.models.Airleg;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;
import com.nearsoft.farandula.models.Segment;
import net.minidev.json.JSONArray;
import okhttp3.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
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
            final Response response = buildHttpClient().newCall(buildRequestForAvail( search )).execute();
            return buildAvailResponse(response.body().byteStream());

        } catch (Exception e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }

    }

    List<Flight> buildAvailResponse(InputStream response) throws IOException {

        List<Flight> resultFlights = new ArrayList<>();
        ReadContext ctx = JsonPath.parse(response);
        JSONArray flightSegments = ctx.read("$..OriginDestinationOption[*]");

        flightSegments.forEach( f ->  {

            List<Airleg> airLegList = new ArrayList<>();
            List<Segment> segments = new ArrayList<>();
            Map<String, Object > generalMap = (Map<String, Object>) f;
            JSONArray jsonSegmentArray = (JSONArray) generalMap.get("FlightSegment");
            jsonSegmentArray.forEach( g -> {

                Map<String, Object > segmentMap = (Map<String, Object>) g;
                Segment seg = new Segment();
                //Todo change segment id and PATH
                seg.setId("");
                seg.setAirlineIconPath("");
                Map<String, Object > airlineData = (Map<String, Object>) segmentMap.get("OperatingAirline");
                Map<String, Object > departureAirportData = (Map<String, Object>) segmentMap.get("DepartureAirport");
                Map<String, Object > arrivalAirportData = (Map<String, Object>) segmentMap.get("ArrivalAirport");
                JSONArray jsonEquipmentArray = (JSONArray) segmentMap.get("Equipment");
                Map<String, Object > equipmentData = (Map<String, Object>) jsonEquipmentArray.get(0);

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

                //TODO
                //check DepartureTimeZone and ArrivalTimeZone to know if is the same zone,
                //if is the same make the operation
                //else check time zone first.
                seg.setTimeFlight( (String)segmentMap.get("ArrivalDateTime")   );
                segments.add(seg);

            });

            if ( !segments.isEmpty() ) {

                //todo the airlegs are more complicated than this
                Airleg leg = new Airleg();
                leg.setId("tempID");
                leg.setArrivalAirportCode(segments.get(0).getDepartureAirportCode() );
                leg.setArrivalDate( segments.get(0).getDepartingDate() );
                leg.setDepartureAirportCode(segments.get(segments.size() - 1).getArrivalAirportCode());
                leg.setDepartingDate( segments.get( segments.size() -1 ).getArrivalDate() );
                leg.setSegments(segments);
                airLegList.add(leg);

                Flight currentFly = new Flight();
                currentFly.setLegs(airLegList);
                //TODO change this PNR and ID
                currentFly.setPNR("tempPNR");
                currentFly.setId("tempID");
                resultFlights.add(currentFly);

            }
        });

        return resultFlights;
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
        //TODO check if we really need final here
        final Creds creds = new Creds(props.getProperty("sabre.client_id"), props.getProperty("sabre.client_secret"));
        TripManager tripManager = new TripManager(creds);


        return tripManager;
    }

    public List<Flight> executeAvail(SearchCommand searchCommand) throws FarandulaException {

        //TODO execute search and

        return this.getAvail(searchCommand);

    }
}