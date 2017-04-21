package com.nearsoft.farandula.models;

import java.util.ArrayList;
import java.util.List;

import static com.nearsoft.farandula.models.PassengerType.ADULT;
import static com.nearsoft.farandula.models.PassengerType.CHILD;

/**
 * Created by pruiz on 4/10/17.
 */
public class Passenger {

    private PassengerType type;

    public static List<Passenger> adults(int totalAdults) {
        return createPassengerList(totalAdults, ADULT);
    }

    public static List<Passenger> children(int totalChildren) {
        return createPassengerList(totalChildren, CHILD);
    }

    private static List<Passenger> createPassengerList(int totalAdults, PassengerType passengerType) {
        List<Passenger> list = new ArrayList<>();
        for (int j = 0; j < totalAdults; j++) {
            Passenger passenger = new Passenger();
            passenger.setType(passengerType);
            list.add(passenger);
        }
        return list;
    }

    public void setType(PassengerType type) {
        this.type = type;
    }

    public PassengerType getType() {
        return type;
    }
}
