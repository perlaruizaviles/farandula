package com.farandula.Service;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by enrique on 8/05/17.
 */

public class FlightServiceTest {
    @Test
    public void validateDateTime(){

        FlightService flightService = new FlightService();

        LocalDateTime result = flightService.parseDateTime("2011-12-03", "10:15:30");

        LocalDateTime expected = LocalDateTime.of(2011, 12, 03, 10, 15, 30);

        Assert.assertEquals( expected, result );

    }
}