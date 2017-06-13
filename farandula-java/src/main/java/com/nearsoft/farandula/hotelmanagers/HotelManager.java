package com.nearsoft.farandula.hotelmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.SearchCommand;

import java.io.IOException;
import java.util.List;
/**
 * Created by pruiz on 6/12/17.
 */
public interface HotelManager {


    List<Object> getAvail(SearchCommand search) throws FarandulaException, IOException;

}
