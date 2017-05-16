package com.farandula.Controller;

import com.farandula.Repositories.FlightRepository;
import com.farandula.Response.FlightResponse;

import com.farandula.Service.FlightService;
import com.farandula.models.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/api/availableFlights")
    public List<List<Flight>> getAvailableFlights(@Param("departureAirportCode") String departureAirportCode,
                                                  @Param("departingDate") String departingDate,
                                                  @Param("departingTime") String departingTime,
                                                  @Param("arrivalAirportCode") String arrivalAirportCode,
                                                  @Param("arrivalDate") String arrivalDate,
                                                  @Param("arrivalTime") String arrivalTime,
                                                  @Param("type") String type,
                                                  @Param("passenger") String passenger){

        return flightService.getResponseFromSearch( departureAirportCode,
                                                    departingDate,
                                                    departingTime,
                                                    arrivalAirportCode,
                                                    arrivalDate,
                                                    arrivalTime,
                                                    type,
                                                    passenger);

    }



}
