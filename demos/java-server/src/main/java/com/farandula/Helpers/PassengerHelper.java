package com.farandula.Helpers;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by antoniohernandez on 5/17/17.
 */
@Component
public class PassengerHelper {

    public AgeManager getPassengersFromString(String passengerStringList) {

        //children:,infants:1;2,infantsOnSeat:,adults:2

        AgeManager ageManager =  new AgeManager();
        //TODO: Complementar con tipo de pasajeros y edades
//        final Pattern pattern = Pattern.compile("[a-z]:\\d,[a-z]:\\d");
//        if (!pattern.matcher(passengerStringList.toLowerCase()).matches()) {
//            throw new IllegalArgumentException("Invalid String");
//        }

        String[] passengerType = passengerStringList.split(",");

        String[] adults = passengerType[3].split(":");
        int numberOfAdults = Integer.parseInt(adults[1]);


        String[] infantsOnSeat = passengerType[2].split(":");
        String[] infantsOnSeatAgesString = (infantsOnSeat.length == 1)
                ? new String[]{}
                : infantsOnSeat[1].split(";");
        int[] infantsOnSeatAges = Arrays.stream(infantsOnSeatAgesString)
                .mapToInt(Integer::parseInt)
                .toArray();

        String[] children = passengerType[0].split(":");
        String[] childrenAgesString = (children.length == 1)
                ? new String[]{}
                : children[1].split(";");
        int[] childrenAges = Arrays.stream(childrenAgesString)
                .mapToInt(Integer::parseInt)
                .toArray();

        String[] infants = passengerType[1].split(":");
        String[] infantsAgesString = (infants.length == 1)
                ? new String[]{}
                : infants[1].split(";");
        int[] infantsAges = Arrays.stream(infantsAgesString)
                .mapToInt(Integer::parseInt)
                .toArray();


        ageManager.setChildAges(childrenAges);
        ageManager.setInfantAges(infantsAges);
        ageManager.setInfantOnSeatAges(infantsOnSeatAges);
        ageManager.setNumberAdults(numberOfAdults);

        return ageManager;
    }
}
