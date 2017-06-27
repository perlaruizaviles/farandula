package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 4/10/17.
 */
public enum PassengerType {
    ADULTS, CHILDREN, INFANTS, INFANTSONSEAT;

    public String getTravelportCode(){
        switch (this){
            case ADULTS:
                return "ADT";

            case CHILDREN:
                return "CHD";

            case INFANTS:
                return "INF";

            case INFANTSONSEAT:
                return "INS";

            default:
                return "ADT";
        }
    }
}
