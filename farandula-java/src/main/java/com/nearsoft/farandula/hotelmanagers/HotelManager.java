package com.nearsoft.farandula.hotelmanagers;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.FlightsSearchCommand;
import com.nearsoft.farandula.models.HotelSearchCommand;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Created by pruiz on 6/12/17.
 */
public interface HotelManager {

    List<Object> getAvail(HotelSearchCommand search) throws FarandulaException, IOException;

    default void validateDate(LocalDateTime date) throws FarandulaException {}
}
