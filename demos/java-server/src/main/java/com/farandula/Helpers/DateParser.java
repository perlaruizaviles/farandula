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
@Component
public class DateParser {

    public static int dateToTimestampSeconds(LocalDateTime date) {
        int timeStamp = (int) (Timestamp.valueOf(date).getTime() / 1000);
        return timeStamp;
    }

    public LocalDateTime parseDateTime(String date, String time) {

        String dateTime = date + "T" + time;

        LocalDateTime departureDateTime = LocalDateTime.parse(
                dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return departureDateTime;
    }

    public List<LocalDateTime> parseStringDatesTimes(String[] dates, String[] times) throws ParameterException {
        if (dates.length != times.length) {
            throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_DATES, "Dates and times don't match");
        } else {
            List<LocalDateTime> localDateTimes = new ArrayList<>();
            for (int i = 0; i < dates.length; i++) {
                LocalDateTime dateTime = this.parseDateTime(dates[i], times[i]);
                localDateTimes.add(dateTime);
            }

            return localDateTimes;
        }
    }

    public List<LocalDateTime> parseStringDatesTimes(String date, String time) throws ParameterException {

        List<LocalDateTime> localDateTimes = new ArrayList<>();
        LocalDateTime dateTime = this.parseDateTime(date, time);

        localDateTimes.add(dateTime);

        return localDateTimes;
    }
}
