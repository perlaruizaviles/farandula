package com.farandula.Controller;

import com.farandula.Repositories.FlightRepository;
import com.farandula.Response.FlightResponse;

import com.farandula.Service.FlightService;
import com.farandula.models.Flight;
import com.farandula.models.FlightItinerary;
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

    @RequestMapping("/api/flights")
    public List<FlightItinerary> getAvailableFlights(@Param("departingAirportCodes") String departingAirportCodes,
                                                     @Param("departingDates") String departingDates,
                                                     @Param("departingTimes") String departingTimes,
                                                     @Param("arrivalAirportCodes") String arrivalAirportCodes,
                                                     @Param("returnDates") String returnDates,
                                                     @Param("returnTimes") String returnTimes,
                                                     @Param("type") String type,
                                                     @Param("passenger") String passenger,
                                                     @Param("cabin") String cabin) {

        return flightService.getResponseFromSearch(departingAirportCodes,
                departingDates,
                departingTimes,
                arrivalAirportCodes,
                returnDates,
                returnTimes,
                type,
                passenger,
                cabin);

    }


}
