package com.nearsoft.farandula.models;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.hotelmanagers.HotelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pruiz on 4/10/17.
 */
public class HotelSearchCommand {

    //LOGGER
    public static Logger LOGGER = LoggerFactory.getLogger(HotelSearchCommand.class);

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Map<GuestType, Integer> guestsMap = new HashMap<>();
    private HotelManager hotelManager;

    //with default values
    private int rooms = 1;

    public HotelSearchCommand(HotelManager hotelManager) {

        this.hotelManager = hotelManager;
    }

    public HotelSearchCommand checkhIn(LocalDateTime checkIn) throws FarandulaException {

        validDates(checkIn);

        this.checkIn = checkIn;

        return this;

    }


    public HotelSearchCommand checkOut(LocalDateTime checkOut) throws FarandulaException {

        validDates(checkOut);

        this.checkOut = checkOut;

        return this;

    }


    public HotelSearchCommand forGuest( GuestType guestType, int number ) throws FarandulaException {

        for ( int i = 0 ; i < number ; i++ ){
            if ( guestsMap.containsKey( guestType ) ){
                guestsMap.put( guestType , guestsMap.get(guestType) + 1 );
            }else{
                guestsMap.put( guestType , 1 );
            }

        }

        return this;

    }

    public HotelSearchCommand rooms(int numberOfRooms) throws FarandulaException {

        this.rooms = numberOfRooms;

        return this;

    }

    public List<Object> execute() throws IOException, FarandulaException {

        LOGGER.info("Search Command info: " + this.toString());
        checkValidSearchCommand();

        return hotelManager.getAvail( this );
    }

    private void checkValidSearchCommand() throws FarandulaException {

        if (this.guestsMap.size() == 0) {
            //case when user does not set any passenger.
            this.guestsMap.put( GuestType.ADULTS, 2 );
        }

        if (this.checkIn == null) {
            throw new FarandulaException(ErrorType.VALIDATION,
                    "Search parameters are not valid, HINT: Check In date is mandatory.");
        }

        if (this.checkOut == null) {
            throw new FarandulaException(ErrorType.VALIDATION,
                    "Search parameters are not valid, HINT: Check Out date is mandatory.");
        }

        if ( this.rooms < 1 ){
            throw new FarandulaException(ErrorType.VALIDATION,
                    "Search parameters are not valid, HINT: rooms parameter has to be greater than 0.");
        }

    }

    private void validDates(LocalDateTime date) throws FarandulaException {

        if (date.isBefore(LocalDateTime.now().minusDays(1))) {
            throw new FarandulaException(ErrorType.VALIDATION, "Is impossible to search in past dates.");
        } else {
            hotelManager.validateDate(date);
        }

    }



}






