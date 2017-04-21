package com.nearsoft.farandula;

import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FlightManager {


    //TODO #9 verify that this is the minimum required/common methods
    List<Flight> getAvail(SearchCommand search) throws FarandulaException;
    //get seats


}
