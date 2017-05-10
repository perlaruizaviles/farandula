package com.farandula.Repositories;


import com.farandula.Flight;
import com.nearsoft.farandula.models.AirLeg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by antoniohernandez on 5/5/17.
 */
@Repository
public interface FlightRepository extends MongoRepository<AirLeg,String>{
    //Find Flight by DepartureCode, DepartureDate, ArrivalCode, ArrivalDate
    List<AirLeg> findByDepartureAirportCodeLikeIgnoreCaseAndDepartingDateLikeIgnoreCaseAndArrivalAirportCodeLikeIgnoreCaseAndArrivalDateLikeIgnoreCase
    (
            @Param("departureAirportCode") String departureAirportCode,
            @Param("departingDate") LocalDateTime departingDate,
            @Param("arrivalAirportCode") String arrivalAirportCode,
            @Param("arrivalDate") LocalDateTime arrivalDate
    );



}
