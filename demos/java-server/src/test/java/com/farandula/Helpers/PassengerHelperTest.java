package com.farandula.Helpers;

import com.nearsoft.farandula.models.Fares;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by enrique on 17/05/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PassengerHelperTest {

    @Autowired
    PassengerHelper passengerHelper;

    @Test
    public void getPassengersFromString() throws Exception {
        String passengerString = "children:3,infants:2,infantsOnSeat:1,adults:2";

        AgeManager ageManager = passengerHelper.getPassengersFromString(passengerString);
        assertEquals(3, ageManager.getChildAges().length);
        assertEquals(2, ageManager.getInfantAges().length);
    }

    @Test
    public void getPassengersFromStringWithNonInfant() throws Exception {
        String passengerString = "children:3,infants:,infantsOnSeat:2,adults:2";

        AgeManager ageManager = passengerHelper.getPassengersFromString(passengerString);
        assertEquals(0, ageManager.getInfantAges().length);
    }
}