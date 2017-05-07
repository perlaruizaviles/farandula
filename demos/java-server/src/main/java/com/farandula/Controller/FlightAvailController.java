package com.farandula.Controller;

import com.farandula.Repositories.FlightRepository;
import com.farandula.Response.FlightResponse;

import com.farandula.Service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by antoniohernandez on 5/4/17.
 */

@RestController
public class FlightAvailController {

    @Autowired
    FlightRepository flightRepository;

    @RequestMapping("/api/avails")
    public FlightResponse getAvailableFlights(@Param("departureAirportCode") String departureAirportCode,
                                              @Param("departingDate") LocalDateTime departingDate,
                                              @Param("arrivalAirportCode") String arrivalAirportCode,
                                              @Param("arrivalDate") LocalDateTime arrivalDate){

        return new FlightService().getResponseFromSearch(
                flightRepository.findByDepartureAirportCodeLikeIgnoreCaseAndDepartingDateLikeIgnoreCaseAndArrivalAirportCodeLikeIgnoreCaseAndArrivalDateLikeIgnoreCase(
                        departureAirportCode,departingDate,arrivalAirportCode,arrivalDate)
        );
    }



}
