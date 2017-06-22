package com.farandula.Helpers;

import com.farandula.Exceptions.ParameterException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoniohernandez on 5/13/17.
 */
public interface DateParser {

    static int dateToTimestampSeconds(LocalDateTime date) {
        int timeStamp = (int) (Timestamp.valueOf(date).getTime() / 1000);
        return timeStamp;
    }

    LocalDateTime parseDateTime(String date, String time);

    List<LocalDateTime> parseStringDatesTimes(String[] dates, String[] times) throws ParameterException;

    List<LocalDateTime> parseStringDatesTimes(String date, String time) throws ParameterException;
}
