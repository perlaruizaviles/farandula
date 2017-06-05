package com.farandula.Helpers;

import com.nearsoft.farandula.utilities.CabinClassParser;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by antoniohernandez on 5/17/17.
 */
@Component
public class PassengerHelper {

    public AgeManager getPassengersFromString(String passengerStringList) {

        //children:0,infants:0,infantsOnSeat:0,adults:2

        AgeManager ageManager = new AgeManager();
        //TODO: Complementar con tipo de pasajeros y edades

        String[] passengerType = passengerStringList.split(",");

        String[] adults = passengerType[3].split(":");
        int numberOfAdults = Integer.parseInt(adults[1]);

        //Infants on Seat parse
        String[] infantsOnSeat = passengerType[2].split(":");
        String[] infantsOnSeatAgesString;

        if (infantsOnSeat.length == 1 || "0".equals(infantsOnSeat[1])) {
            infantsOnSeatAgesString = new String[]{};
        } else {
            int numberOfInfantsOnSeat = Integer.parseInt(infantsOnSeat[1]);
            infantsOnSeatAgesString = new String[numberOfInfantsOnSeat];
            for (int i = 0; i < numberOfInfantsOnSeat; i++) {
                infantsOnSeatAgesString[i] = "2";
            }
        }

        int[] infantsOnSeatAges = Arrays.stream(infantsOnSeatAgesString)
                .mapToInt(Integer::parseInt)
                .toArray();

        //Children parse
        String[] children = passengerType[0].split(":");
        String[] childrenAgesString;

        if (children.length == 1 || "0".equals(children[1])) {
            childrenAgesString = new String[]{};
        } else {
            int numberOfChildren = Integer.parseInt(children[1]);
            childrenAgesString = new String[numberOfChildren];
            for (int i = 0; i < numberOfChildren; i++) {
                childrenAgesString[i] = "8";
            }
        }

        int[] childrenAges = Arrays.stream(childrenAgesString)
                .mapToInt(Integer::parseInt)
                .toArray();

        //Infants parse
        String[] infants = passengerType[1].split(":");
        String[] infantsAgesString;

        if (infants.length == 1 || "0".equals(infants[1])) {
            infantsAgesString = new String[]{};
        } else {
            int numberOfInfants = Integer.parseInt(infants[1]);
            infantsAgesString = new String[numberOfInfants];
            for (int i = 0; i < numberOfInfants; i++) {
                infantsAgesString[i] = "2";
            }
        }

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
