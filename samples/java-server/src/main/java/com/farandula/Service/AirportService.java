package com.farandula.Service;

import com.farandula.Repositories.AirportRepository;
import com.farandula.models.Airport;
import com.farandula.Response.Response;
import com.farandula.models.FlightItinerary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by enrique on 1/05/17.
 */
public class AirportService {

    public List<Airport> getResponseFromSearch(AirportRepository airportRepository, String pattern){
        List<Airport> list = airportRepository.findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase
                (
                        pattern,
                        pattern,
                        pattern
                );

        return list;
    }

}
