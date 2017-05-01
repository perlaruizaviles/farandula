package com.farandula.Repositories;

import com.farandula.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by enrique on 1/05/17.
 */
@Repository
public interface AirportRepository extends MongoRepository<Airport, String>{

    List<Airport> findByCityLike(@Param("city") String city);

}
