package com.farandula.Helpers;

import com.nearsoft.farandula.utilities.CabinClassParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by antoniohernandez on 5/17/17.
 */
@Component
public interface PassengerHelper {
    AgeManager getPassengersFromString(String passengerStringList);
}
