package com.farandula.Helpers;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

/**
 * Created by antoniohernandez on 5/15/17.
 */
public class DateParserTest {
    @Test
    public void dateToTimestampSeconds() throws Exception {
        //uses local time obviously
        LocalDateTime specificDate = LocalDateTime.of(2017, Month.DECEMBER, 1, 10, 10 , 30);
        assertEquals(1512148230, DateParser.dateToTimestampSeconds(specificDate) );
    }

}