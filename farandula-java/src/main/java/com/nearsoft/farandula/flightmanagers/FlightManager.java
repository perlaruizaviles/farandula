package com.nearsoft.farandula.flightmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;

import java.util.List;

public interface FlightManager {


    //TODO #9 verify that this is the minimum required/common methods
    List<Flight> getAvail(SearchCommand search) throws FarandulaException;
    //get seats


}
