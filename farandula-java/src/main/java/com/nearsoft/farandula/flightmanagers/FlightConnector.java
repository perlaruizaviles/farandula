package com.nearsoft.farandula.flightmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.FlightsSearchCommand;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightConnector {

    List<Itinerary> getAvail(FlightsSearchCommand search) throws FarandulaException, IOException;

    default void validateDate(LocalDateTime date) throws FarandulaException {}

}
