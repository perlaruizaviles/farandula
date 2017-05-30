package com.farandula.Helpers;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by Admin on 5/29/17.
 */
public class AiportsSourceTest {

    @Test
    public void containsParsedAirports() {
        assertEquals(AiportsSource.getAirportsCount() >= 5654, true);
    }
}
