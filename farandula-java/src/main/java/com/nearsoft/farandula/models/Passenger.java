package com.nearsoft.farandula.models;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;

import java.util.ArrayList;
import java.util.List;

import static com.nearsoft.farandula.models.PassengerType.*;

/**
 * Created by pruiz on 4/10/17.
 */
public class Passenger {

    private PassengerType type;
    private int age;

    public static List<Passenger> adults(int totalAdults) {
        return createPassengerList(totalAdults, ADULTS);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static List<Passenger> children(int[] ages ) throws FarandulaException {

        return CreatePassengerListWithAGe(ages , PassengerType.CHILDREN);

    }

    public static List<Passenger> infants(int[] totalInfants) throws FarandulaException {
        return CreatePassengerListWithAGe(totalInfants, INFANTS);
    }


    public static List<Passenger> infantsOnSeat(int[] totalInfantsOnseat) throws FarandulaException {
        return CreatePassengerListWithAGe(totalInfantsOnseat, INFANTSONSEAT);
    }

    private static List<Passenger> CreatePassengerListWithAGe(int[] ages, PassengerType type) throws FarandulaException {

        List<Passenger> list = new ArrayList<>();

        if ( !validAge( type, ages ) ){
            throw new FarandulaException( ErrorType.VALIDATION, "Invalid age for " + type.toString() );
        }

        for (int j = 0; j < ages.length; j++) {
            Passenger passenger = new Passenger();
            passenger.setType(type);
            passenger.setAge( ages[j] );
            list.add(passenger);
        }
        return list;
    }

    private static boolean validAge(PassengerType type, int[] ages) {

        for ( int age : ages ){

            if ( type == PassengerType.CHILDREN ){

                return age < 18;

            }

            if ( type == PassengerType.INFANTS || type == PassengerType.INFANTSONSEAT ){

                return age < 3;

            }

        }

        return true;
    }

    private static List<Passenger> createPassengerList(int totalPassengers, PassengerType passengerType) {
        List<Passenger> list = new ArrayList<>();
        for (int j = 0; j < totalPassengers; j++) {
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

    @Override
    public String toString() {
        return "Passenger{" +
                "type=" + type +
                ", age=" + age +
                '}';
    }
}
