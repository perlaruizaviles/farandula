package com.nearsoft.farandula.flightmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.SearchCommand;

import java.util.List;

public interface FlightManager {

    List<AirLeg> getAvail(SearchCommand search) throws FarandulaException;
    //get seats


}
