package com.farandula.Service;

import com.farandula.Repositories.AirportRepository;
import com.farandula.models.Airport;
import com.farandula.Response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by enrique on 1/05/17.
 */
public class AirportService {

    @Autowired
    AirportRepository airportRepository;

    public List<Airport> getResponseFromSearch(String pattern){
        return airportRepository.findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase
                (
                        pattern,
                        pattern,
                        pattern
                );
    }

}
