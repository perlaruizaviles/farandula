package com.farandula.Controller;

import com.farandula.Repositories.AirportRepository;
import com.farandula.Response.Response;
import com.farandula.Service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by enrique on 1/05/17.
 */
@RestController
public class AirportController {

    @Autowired
    AirportRepository airportRepository;

    @RequestMapping("/api/airports")
    public Response searchAirport(@Param("pattern") String pattern, @Param("key") String key){

        return new AirportService().getResponseFromSearch(
                airportRepository.
                    findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase(pattern, pattern, pattern)
        );
    }
}
