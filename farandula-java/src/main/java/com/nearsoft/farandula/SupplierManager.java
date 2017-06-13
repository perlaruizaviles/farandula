package com.nearsoft.farandula;

import com.nearsoft.farandula.flightmanagers.FlightConnector;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightConnector;
import com.nearsoft.farandula.hotelmanagers.HotelManager;
import com.nearsoft.farandula.hotelmanagers.sabre.SabreHotelManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pruiz on 6/13/17.
 */
public class SupplierManager {
    private List<FlightConnector> flightConnectors = new ArrayList<>();
    private List<HotelManager> hotelConnectors = new ArrayList<>();

    public void addSFlightConnector(FlightConnector aSupplier) {
            flightConnectors.add(aSupplier);
    }

    //TODO this should change, to something smarter
    public FlightConnector getDefaultFlightConnector() {

        if (flightConnectors.isEmpty()){
            flightConnectors.add(new SabreFlightConnector());
        }
        return flightConnectors.get(0);
    }




    public void addHotelConnector(HotelManager connector) {

        hotelConnectors.add(connector);
    }

    public HotelManager getDefaultHotelConnector() {
        if(hotelConnectors.isEmpty()){
            hotelConnectors.add(new SabreHotelManager());
        }
        return hotelConnectors.get(0);
    }
}
