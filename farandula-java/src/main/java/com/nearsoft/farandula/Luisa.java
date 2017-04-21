package com.nearsoft.farandula;

import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;

import java.util.function.Supplier;

/**
 * Created by pruiz on 4/10/17.
 */
public class Luisa {

    public static SearchCommand findMeFlights() {
        return new SearchCommand(getInstance());
    }

    private static Supplier<Manager> supplier = () -> new SabreTripManager(null);

    public static void setSupplier(Supplier<Manager> aSupplier) {
        supplier = aSupplier;
    }

    public static Manager getInstance() {
        return supplier.get();
    }

}
