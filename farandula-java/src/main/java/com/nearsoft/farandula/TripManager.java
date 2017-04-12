package com.nearsoft.farandula;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;
import net.minidev.json.JSONArray;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


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

        ReadContext ctx = JsonPath.parse(response);
        //System.out.println(ctx.read("$.OTA_AirLowFareSearchRS.PricedItinCount").toString());
        JSONArray flightSegments = ctx.read("$..OriginDestinationOption[*]");

        flightSegments.forEach( f ->  {
            Map<String, Object > map = (Map<String, Object>) f;
            //System.out.println( map.get("ElapsedTime" ) );
        });

        //TODO create Flight model

        //TODO
        List<Flight> resultList = new ArrayList<>();

        return resultList;
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