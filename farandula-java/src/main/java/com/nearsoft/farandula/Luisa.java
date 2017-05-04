package com.nearsoft.farandula;

import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightManager;
import com.nearsoft.farandula.models.SearchCommand;

import java.util.function.Supplier;

/**
 * Created by pruiz on 4/10/17.
 */
public class Luisa {


    public static SearchCommand findMeFlights() {
        return new SearchCommand(getInstance());
    }

    private static Supplier<FlightManager> supplier = () -> new SabreFlightManager();

    public static void setSupplier(Supplier<FlightManager> aSupplier) {
        supplier = aSupplier;
    }

    public static FlightManager getInstance() {
        return supplier.get();
    }

}
