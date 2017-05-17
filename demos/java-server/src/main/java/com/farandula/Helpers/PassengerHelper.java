package com.farandula.Helpers;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by antoniohernandez on 5/17/17.
 */
@Component
public class PassengerHelper {

    public int[][] getPassengersFromString(String passengerStringList) {

        //children:7;3;4,infants:1;2,infantsOnSeat:2,adults:2



        //TODO: Complementar con tipo de pasajeros y edades
//        final Pattern pattern = Pattern.compile("[a-z]:\\d,[a-z]:\\d");
//        if (!pattern.matcher(passengerStringList.toLowerCase()).matches()) {
//            throw new IllegalArgumentException("Invalid String");
//        }

        String[] passengerType = passengerStringList.split(",");
        String[] adults = passengerType[3].split(":");
        String[] infantsOnSeat = passengerType[2].split(":");
        String[] infants = passengerType[1].split(":");
        String[] children = passengerType[0].split(":");

        String[] childrenAgesString = children[1].split(";");
        String[] infantsAgesString = infants[1].split(";");
        String[] infantsOnSeatAgesString = infantsOnSeat[1].split(";");

        int[] childrenAges = Arrays.stream(childrenAgesString)
                .mapToInt(age -> Integer.parseInt(age))
                .toArray();
        int[] infantsAges = Arrays.stream(infantsAgesString)
                .mapToInt(age -> Integer.parseInt(age))
                .toArray();
        int[] infantsOnSeatAges = Arrays.stream(infantsOnSeatAgesString)
                .mapToInt(age -> Integer.parseInt(age))
                .toArray();

        int[] adultsAges = {Integer.parseInt(adults[1])};

        int[][] numberOfPassengers = {childrenAges,infantsAges,infantsOnSeatAges,adultsAges};

        return  numberOfPassengers;
    }
}
