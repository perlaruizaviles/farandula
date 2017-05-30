package com.farandula.Exceptions;

/**
 * Created by enrique on 24/05/17.
 */
public class ParameterException extends Exception {

    public enum ParameterErrorType{
        ERROR_ON_DATES, ERROR_ON_AIRPORT_CODES
    }

    ParameterErrorType parameterErrorType;

    public ParameterException(ParameterErrorType errorType, String message){
        super(message);
        this.parameterErrorType = errorType;
    }

    @Override
    public String toString(){
        return String.format("Error Type: %s \n %s", parameterErrorType, super.toString());
    }
}
