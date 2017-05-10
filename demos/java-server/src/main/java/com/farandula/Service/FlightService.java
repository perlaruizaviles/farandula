package com.farandula.Service;

import com.farandula.Repositories.AirportRepository;
import com.farandula.Response.FlightResponse;
import com.farandula.models.Airport;
import com.farandula.models.Flight;
import com.farandula.models.FlightSegment;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.travelport.TravelportFlightManager;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.FlightType;
import com.nearsoft.farandula.models.Passenger;
import com.nearsoft.farandula.models.Segment;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    static AirportRepository airportRepository;

    public FlightResponse getResponseFromSearch(String departureAirportCode,
                                                String departingDate,
                                                String departingTime,
                                                String arrivalAirportCode,
                                                String arrivalDate,
                                                String arrivalTime,
                                                String type,
                                                String passenger) {

        if( FlightService.validIataLength(departureAirportCode) && FlightService.validIataLength(arrivalAirportCode) ){

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
                Integer[] numberOfPassengers = getPassengersFromString(passenger);

                if( "oneWay".equals(type) ){

                    List<AirLeg> flights = Luisa.findMeFlights()
                            .from( departureAirportCode )
                            .to( arrivalAirportCode )
                            .departingAt( departDateTime )
                            .returningAt( arrivalDateTime )
                            .forPassegers(Passenger.adults(numberOfPassengers[0]))
                            .forPassegers(Passenger.children(numberOfPassengers[1]))
                            .limitTo(2)
                            .execute();

                    return FlightResponse.getResponseInstance(200, "Response", this.getFlightsFromAirlegList(flights));


                } else{

                    List<AirLeg> flights = Luisa.findMeFlights()
                            .from( departureAirportCode )
                            .to( arrivalAirportCode )
                            .departingAt( departDateTime )
                            .returningAt( arrivalDateTime )
                            .type(FlightType.ROUNDTRIP)
                            .limitTo(2)
                            .forPassegers(Passenger.adults(numberOfPassengers[0]))
                            .forPassegers(Passenger.children(numberOfPassengers[1]))
                            .execute();

                    List<AirLeg> departLegs = this.getDepartAirLegs(flights);
                    List<AirLeg> returnLegs = this.getReturnAirLegs(flights);
                    List<Flight> departFlights =  this.getFlightsFromAirlegList(departLegs);
                    List<Flight> returnFlights =  this.getFlightsFromAirlegList(returnLegs);
                    return FlightResponse.getResponseInstance(200, "Response", departFlights, returnFlights);

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

    public static List<Flight> getFlightsFromAirlegList(List<AirLeg> airLegList){
        List<Flight> flights = new ArrayList<>();
        for (AirLeg airleg:airLegList) {
            Airport departureAirport = airportRepository.findByIataLikeIgnoreCase(airleg.getDepartureAirportCode()).get(0);
            Airport arrivalAirport = airportRepository.findByIataLikeIgnoreCase(airleg.getArrivalAirportCode()).get(0);
            LocalDateTime departureDate = airleg.getDepartingDate();
            LocalDateTime arrivalDate = airleg.getArrivalDate();
            List<FlightSegment> flightSegments =  new ArrayList<>();
            for (Segment segment:airleg.getSegments()) {
                Airport departureSegmentAirport = airportRepository.findByIataLikeIgnoreCase(segment.getDepartureAirportCode()).get(0);
                Airport arrivalSegmentAirport = airportRepository.findByIataLikeIgnoreCase(segment.getArrivalAirportCode()).get(0);
                LocalDateTime departureSegmentDate = segment.getDepartingDate();
                LocalDateTime arrivalSegmentDate = segment.getArrivalDate();
                long duration = segment.getDuration();
                FlightSegment flightSegment = new FlightSegment(departureSegmentAirport, departureSegmentDate, arrivalSegmentAirport,
                        arrivalSegmentDate, duration);
                flightSegments.add(flightSegment);

            }

            Flight flight = new Flight(departureAirport, departureDate, arrivalAirport,
                    arrivalDate, flightSegments);
            flights.add(flight);


        }
        return  flights;
    }


    public static boolean validIataLength(String iata) {
        return (iata.length() == 3) || (iata.length() == 2);
    }

    public static Integer[] getPassengersFromString(String passengerStringList) {

        //TODO: Quitar los comments
//        final Pattern pattern = Pattern.compile("[a-z]:\\d,[a-z]:\\d");
//        if (!pattern.matcher(passengerStringList.toLowerCase()).matches()) {
//            throw new IllegalArgumentException("Invalid String");
//        }

        String[] passengerType = passengerStringList.split(",");
        String[] adults = passengerType[1].split(":");
        String[] children = passengerType[0].split(":");
        Integer[] numberOfPassengers = {Integer.parseInt(adults[1]),Integer.parseInt(children[1])};
        return  numberOfPassengers;
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
