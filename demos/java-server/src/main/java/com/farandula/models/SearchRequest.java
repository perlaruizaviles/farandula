package com.farandula.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by emote on 29/05/17.
 */
public class SearchRequest {

    @NotNull
    @Size(min = 2, max = 3, message = "Airport code length must be two or three")
    @Pattern(regexp = "[A-Z]")
    String departAirportCode;

    @NotNull
    @Size(min = 10, max = 10, message = "Departure date length must be 10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String departingDate;

    @NotNull
    @Size(min = 8, max = 8)
    @DateTimeFormat(pattern = "HH:mm:ss")
    String departingTime;

    @NotNull
    @Size(min = 2, max = 3)
    String arrivalAirportCode;

    @Size(min = 10, max = 10, message = "Returning date length must be 10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String returningDate;

    @Size(min = 8, max = 8)
    @DateTimeFormat(pattern = "HH:mm:ss")
    String returningTime;

    @NotNull
    @Pattern(regexp = "[a-z]")
    String type;

    @NotNull
    @Pattern(regexp = "[a-z]+:+")
    String passenger;

    @NotNull
    String cabin;

    @NotNull
    String limit;
}
