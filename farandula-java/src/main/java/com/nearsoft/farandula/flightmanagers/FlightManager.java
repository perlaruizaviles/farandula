package com.nearsoft.farandula.flightmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.SearchCommand;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightManager {

    List<Itinerary> getAvail(SearchCommand search) throws FarandulaException;

    default void validateDate(LocalDateTime date) throws FarandulaException {}
    //get seats


}
