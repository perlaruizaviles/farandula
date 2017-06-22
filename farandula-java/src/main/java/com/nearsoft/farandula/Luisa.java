package com.nearsoft.farandula;

import com.nearsoft.farandula.flightmanagers.FlightConnector;
import com.nearsoft.farandula.hotelmanagers.HotelManager;
import com.nearsoft.farandula.models.FlightsSearchCommand;
import com.nearsoft.farandula.models.HotelSearchCommand;

/**
 * Created by pruiz on 4/10/17.
 */
public class Luisa {

    private  SupplierManager manager;

    private Luisa(SupplierManager manager) {

        this.manager = manager;
    }

    public FlightsSearchCommand findMeFlights() {
        return new FlightsSearchCommand(manager.getDefaultFlightConnector());
    }

    public HotelSearchCommand findMeHotels() {


        return new HotelSearchCommand( manager.getDefaultHotelConnector());
    }



    public static Luisa using(FlightConnector aSupplier) {
        SupplierManager manager  = new SupplierManager();
         manager.addSFlightConnector(aSupplier);
        return new Luisa(manager);
    }


    public static Luisa using(HotelManager aSupplier) {
        SupplierManager manager  = new SupplierManager();
        manager.addHotelConnector(aSupplier);
        return  new Luisa(manager);
    }



}
