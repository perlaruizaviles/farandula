package com.farandula.Service;

import com.farandula.Repositories.AirportRepository;
import com.farandula.Response.Response;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by enrique on 1/05/17.
 */
public class AirportService {

    public Response getResponseFromSearch( String query, AirportRepository airportRepository ){
        return Response.getResponseInstance( 200, "Results", airportRepository.findByCityLike( query ) );
    }

}
