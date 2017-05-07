package com.farandula.Service;

import com.farandula.Airport;
import com.farandula.Flight;
import com.farandula.Response.FlightResponse;
import com.farandula.Response.Response;
import com.nearsoft.farandula.models.AirLeg;

import java.util.List;

/**
 * Created by antoniohernandez on 5/5/17.
 */
public class FlightService {
    public FlightResponse getResponseFromSearch(List<AirLeg> flights){
        return FlightResponse.getResponseInstance( 200, "Results", flights );
    }
}
