package com.nearsoft.farandula.hotelmanagers.sabre;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.hotelmanagers.HotelManager;
import com.nearsoft.farandula.models.SearchCommand;

import java.io.IOException;
import java.util.List;

/**
 * Created by pruiz on 6/12/17.
 */
public class SabreHotelManager implements HotelManager {

    @Override
    public List<Object> getAvail(SearchCommand search) throws FarandulaException, IOException {
        return null;
    }
}
