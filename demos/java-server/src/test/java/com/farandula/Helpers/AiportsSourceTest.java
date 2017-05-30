package com.farandula.Helpers;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by Admin on 5/29/17.
 */
public class AiportsSourceTest {

    @Test
    public void containsParsedAirports() {
        assertTrue(AirportsSource.getAirportsCount() >= 5654);
    }

    @Test
    public void containsParticularAirport(){
        assertEquals("Chihuahua", AirportsSource.getAirport("CUU").getCity());
    }

    @Test
    public void isSecureGetAirport(){
        AirportsSource.getAirport("CUU").setCity("Delicias");

        assertEquals("Chihuahua", AirportsSource.getAirport("CUU").getCity());
    }
}
