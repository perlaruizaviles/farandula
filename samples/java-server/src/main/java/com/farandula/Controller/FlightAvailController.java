package com.farandula.Controller;

import com.farandula.Repositories.FlightRepository;
import com.farandula.Response.FlightResponse;

import com.farandula.Service.FlightService;
import com.farandula.models.Flight;
import com.farandula.models.FlightItinerary;
import com.farandula.models.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by antoniohernandez on 5/4/17.
 */
@CrossOrigin(origins = "*")
@RestController
public class FlightAvailController {

    @Autowired
    FlightService flightService;

    @RequestMapping("/api/flights")
    public List<FlightItinerary> getAvailableFlights(@Valid SearchRequest request) {
        return flightService.getResponseFromSearch( request );
    }


}
