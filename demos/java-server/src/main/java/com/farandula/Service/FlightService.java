package com.farandula.Service;

import com.farandula.Airport;
import com.farandula.Response.FlightResponse;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.travelport.TravelportFlightManager;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.FlightType;
import com.nearsoft.farandula.models.Passenger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by antoniohernandez on 5/5/17.
 */
@Component
public class FlightService {
    public FlightResponse getResponseFromSearch(String departureAirportCode,
                                                String departingDate,
                                                String departingTime,
                                                String arrivalAirportCode,
                                                String arrivalDate,
                                                String arrivalTime,
                                                String type,
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

                if( type.equals("round") ){
                    List<AirLeg> flights = Luisa.findMeFlights()
                            .from( departureAirportCode )
                            .to( arrivalAirportCode )
                            .departingAt( departDateTime )
                            .returningAt( arrivalDateTime )
                            .type(FlightType.ROUNDTRIP)
                            .limitTo(2)
                            .forPassegers(Passenger.adults(2))
                            .execute();

                    List<AirLeg> departLegs = this.getDepartAirLegs(flights);
                    List<AirLeg> returnLegs = this.getReturnAirLegs(flights);

                    return FlightResponse.getResponseInstance(200, "Response", departLegs, returnLegs);
                }
                else{
                    List<AirLeg> flights = Luisa.findMeFlights()
                            .from( departureAirportCode )
                            .to( arrivalAirportCode )
                            .departingAt( departDateTime )
                            .returningAt( arrivalDateTime )
                            .forPassegers(Passenger.adults(2))
                            .limitTo(2)
                            .execute();

                    return FlightResponse.getResponseInstance(200, "Response", flights);
                }

                //TODO Parse response for passengers

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

        return null;
    }

    public static boolean checkIata(String iata) {
        return (iata.length() == 3) || (iata.length() == 2);
    }

    public static LocalDateTime parseDateTime(String date, String time) {

        String dateTime = date + "T" + time;

        LocalDateTime departureDateTime = LocalDateTime.parse(
                dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return departureDateTime;
    }

    public List<AirLeg> getDepartAirLegs( List<AirLeg>  legs){

        return IntStream.range(0, legs.size())
                .filter( i -> i%2 == 0 )
                .mapToObj( i -> legs.get(i) )
                .collect(Collectors.toList());

    }

    public List<AirLeg> getReturnAirLegs( List<AirLeg>  legs){

        return IntStream.range(0, legs.size())
                .filter( i -> i%2 == 1 )
                .mapToObj( i -> legs.get(i) )
                .collect(Collectors.toList());
    }
}
