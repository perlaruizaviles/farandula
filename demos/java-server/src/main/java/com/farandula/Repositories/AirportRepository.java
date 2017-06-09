package com.farandula.Repositories;

import com.farandula.models.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by enrique on 1/05/17.
 */
@Repository
public interface AirportRepository extends MongoRepository<Airport, String>{

    //Find an airport by city name
    List<Airport> findByCityLikeIgnoreCase(@Param("city") String city);

    //Find Airport by City name, Airport name or Iata code
    List<Airport> findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase
            (
                @Param("city") String city,
                @Param("name") String name,
                @Param("iata") String iata
            );

    List<Airport> findByIataLikeIgnoreCase(@Param("iata") String iata);

}
