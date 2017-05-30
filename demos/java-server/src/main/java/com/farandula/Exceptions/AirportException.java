package com.farandula.Exceptions;

/**
 * Created by Admin on 5/19/17.
 */
public class AirportException extends Exception {

    AirportErrorType airportErrorType;

    public AirportException(AirportErrorType error, String message) {
        super(message);
        this.airportErrorType = error;
    }

    @Override
    public String toString() {
        return String.format("Error Type: [%s] \n %s", airportErrorType, super.toString());
    }

    public enum AirportErrorType {
        AIRPORT_NOT_FOUND
    }

}
