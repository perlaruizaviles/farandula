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
        String passengerString = "children:7;3;4,infants:1;2,infantsOnSeat:2,adults:2";

        int[][] numberOfPassengers = passengerHelper.getPassengersFromString(passengerString);
        assertEquals(3, numberOfPassengers[0][1]);
        assertEquals(2, numberOfPassengers[1][1]);
    }

}