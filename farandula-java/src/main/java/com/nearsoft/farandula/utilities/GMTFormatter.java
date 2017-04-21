package com.nearsoft.farandula.utilities;

/**
 * Created by pruiz on 4/16/17.
 */
public class GMTFormatter {

    public static String GMTformatter( String GMT_ZONE){

        double GMT_ZONE_VALUE = Double.parseDouble(GMT_ZONE);
        int GMT_ZONE_INTEGER = (int)Math.floor(Math.abs(GMT_ZONE_VALUE));
        String GMT_ZONE_FORMATTED = (GMT_ZONE_VALUE < 0.0 ? "-" : "+") // "-" o "+" this helps to set the -' or '+' signal
            + String.format("%02d", GMT_ZONE_INTEGER)  // adding 0 if is necessary
            + ":"
            + (GMT_ZONE_VALUE % 1 == 0 ? "00" : "30");   // "00" or "30" depending of the decimal part

        return GMT_ZONE_FORMATTED;

    }

}
