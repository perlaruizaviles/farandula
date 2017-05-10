package com.farandula.Service;

import com.farandula.models.Airport;
import com.farandula.Response.Response;

import java.util.List;

/**
 * Created by enrique on 1/05/17.
 */
public class AirportService {

    public Response getResponseFromSearch(List<Airport> airports){
        return Response.getResponseInstance( 200, "Results", airports );
    }

}
