package com.farandula.Helpers.Implementations;

import com.farandula.Helpers.AgeManager;
import com.farandula.Helpers.PassengerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Admin on 6/5/17.
 */
@Component
public class PassengerHelperImpl implements PassengerHelper {

    private int[] parsePassengerAges(String passengerType, String type) {

        String[] passenger = passengerType.split(":");
        int[] passengerAges;

        if (passenger.length == 1 || "0".equals(passenger[1])) {
            passengerAges = new int[]{};
        } else {
            int numPassengers = Integer.parseInt(passenger[1]);
            passengerAges = new int[numPassengers];
            for (int i = 0; i < numPassengers; i++) {

                int age = 18;

                switch (type) {
                    case "infant":
                    case "infantOnSeat":
                        age = 2;
                        break;

                    case "children":
                        age = 8;
                }

                passengerAges[i] = age;
            }
        }
        return passengerAges;
    }

    public AgeManager getPassengersFromString(String passengerStringList) {

        //children:0,infants:0,infantsOnSeat:0,adults:2
        AgeManager ageManager = new AgeManagerImpl();
        //TODO: Complementar con tipo de pasajeros y edades

        String[] passengerType = passengerStringList.split(",");

        //Children parse
        int[] childrenAges = parsePassengerAges(passengerType[0], "children");

        //Infants parse
        int[] infantsAges = parsePassengerAges(passengerType[1], "infant");

        //Infants on Seat parse
        int[] infantsOnSeatAges = parsePassengerAges(passengerType[2], "infantOnSeat");

        String[] adults = passengerType[3].split(":");
        int numberOfAdults = Integer.parseInt(adults[1]);

        ageManager.setChildAges(childrenAges);
        ageManager.setInfantAges(infantsAges);
        ageManager.setInfantOnSeatAges(infantsOnSeatAges);
        ageManager.setNumberAdults(numberOfAdults);

        return ageManager;
    }

}
