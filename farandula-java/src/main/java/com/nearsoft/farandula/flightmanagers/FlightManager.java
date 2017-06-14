package com.nearsoft.farandula.flightmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.SearchCommand;

import javax.xml.soap.SOAPException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightManager {

    /**
     * Returns a List of Itinerary elements starting from a search command object. This method
     * has implementetion on
     * {@link com.nearsoft.farandula.flightmanagers.amadeus.AmadeusFlightManager#getAvail(SearchCommand)
     * AmadeusFlightManager.getAvail(SearchCommand search)},
     * {@link com.nearsoft.farandula.flightmanagers.travelport.TravelportFlightManager#getAvail(SearchCommand)
     * TravelportFlightManager.getAvail(SearchCommand search)} and
     * {@link com.nearsoft.farandula.flightmanagers.sabre.SabreFlightManager#getAvail(SearchCommand)
     * SabreFlightManager.getAvail(SearchCommand search)}
     *
     * @param search SearchCommand
     * @return List<Itinerary>
     * @throws FarandulaException
     * @throws IOException
     */
    List<Itinerary> getAvail(SearchCommand search) throws FarandulaException, IOException;

    /**
     *
     * @param date LocalDateTime
     * @throws FarandulaException
     */
    default void validateDate(LocalDateTime date) throws FarandulaException {}
    //get seats

}
