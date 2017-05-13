package com.farandula.Helpers;

import java.time.LocalDateTime;
import java.sql.Timestamp;
/**
 * Created by antoniohernandez on 5/13/17.
 */
public class DateParser {

    public static int dateToTimestampSeconds(LocalDateTime date) {
        int timeStamp = (int)(Timestamp.valueOf(date).getTime()/1000);
        return timeStamp;
    }
}
