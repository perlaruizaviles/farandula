package com.nearsoft.farandula.flightmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.SearchCommand;

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightManager {

    List<Itinerary> getAvail(SearchCommand search) throws FarandulaException, IOException;

    default void validateDate(LocalDateTime date) throws FarandulaException {}
    //get seats

}
