package com.nearsoft.farandula;

import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightManager;
import com.nearsoft.farandula.hotelmanagers.HotelManager;
import com.nearsoft.farandula.hotelmanagers.sabre.SabreHotelManager;
import com.nearsoft.farandula.models.FlightsSearchCommand;
import com.nearsoft.farandula.models.HotelSearchCommand;

import java.util.function.Supplier;

/**
 * Created by pruiz on 4/10/17.
 */
public class Luisa {


    public static FlightsSearchCommand findMeFlights() {
        return new FlightsSearchCommand(getInstance());
    }


    public static HotelSearchCommand findMeHotels() {
        return new HotelSearchCommand( new SabreHotelManager() );
    }

    private static Supplier<FlightManager> supplier = () -> new SabreFlightManager();

    public static void setSupplier(Supplier<FlightManager> aSupplier) {
        supplier = aSupplier;
    }

    public static FlightManager getInstance() {
        return supplier.get();
    }

}
