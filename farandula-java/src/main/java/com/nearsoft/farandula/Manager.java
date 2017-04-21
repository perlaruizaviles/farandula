package com.nearsoft.farandula;

import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Manager {


    //TODO #9 verify that this is the minimum required/common methods
    OkHttpClient buildHttpClient( );
    List<Flight> executeAvail(SearchCommand searchCommand ) throws FarandulaException;
    List<Flight> buildAvailResponse(InputStream response) throws IOException;
    List<Flight> getAvail(SearchCommand search) throws FarandulaException;
    InputStream sendRequest( Request request ) throws IOException, FarandulaException;
    List<Object> getResponse( InputStream response );

}
