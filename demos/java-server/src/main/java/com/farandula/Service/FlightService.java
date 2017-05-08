package com.farandula.Service;

import com.farandula.Response.FlightResponse;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.travelport.TravelportFlightManager;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.FlightType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by antoniohernandez on 5/5/17.
 */
public class FlightService {
    public FlightResponse getResponseFromSearch(String departureAirportCode, String departingDate, String departingTime,
                                                String arrivalAirportCode,
                                                String arrivalDate,
                                                String arrivalTime,
                                                String passenger) {

        if( FlightService.checkIata(departureAirportCode) && FlightService.checkIata(arrivalAirportCode) ){

            LocalDateTime departDateTime = FlightService.parseDateTime(departingDate, departingTime);
            LocalDateTime arrivalDateTime = FlightService.parseDateTime(arrivalDate, arrivalTime);

            Luisa.setSupplier(() -> {
                try {
                    return new TravelportFlightManager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            try{
                List<AirLeg> flights = Luisa.findMeFlights()
                        .from( departureAirportCode )
                        .to( arrivalAirportCode )
                        .departingAt( departDateTime )
                        .returningAt( arrivalDateTime )
                        .limitTo(2)
                        .execute();

            }
            catch (FarandulaException e){
                Logger.getAnonymousLogger().warning(e.toString());
            }
            catch (IOException o){
                Logger.getAnonymousLogger().warning(o.toString());
            }

        }
        else{
            return null;
        }

    }

    public static boolean checkIata(String iata) {
        return (iata.length() > 3) || iata.length() < 2;
    }

    public static LocalDateTime parseDateTime(String date, String time) {

        String dateTime = date + "T" + time;

        LocalDateTime departureDateTime = LocalDateTime.parse(
                dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return departureDateTime;

    }
}
