package com.nearsoft.farandula.utilities;

/**
 * Created by pruiz on 4/16/17.
 */
public class GMTFormatter {

    public static String GMTformatter( String GMT_ZONE){

        //format code
        String GMT_ZONE_FORMATTED="";
        if ( GMT_ZONE.startsWith("-")  ){
            GMT_ZONE_FORMATTED = "-";
            GMT_ZONE = GMT_ZONE.substring(1);
        }else{
            GMT_ZONE_FORMATTED = "+";
        }

        if ( GMT_ZONE.substring( 0,  GMT_ZONE.indexOf(".")).length() == 1 ){
            GMT_ZONE_FORMATTED += "0" + GMT_ZONE.substring( 0,  GMT_ZONE.indexOf(".")) ;
        }else {
            GMT_ZONE_FORMATTED += GMT_ZONE.substring(0, GMT_ZONE.indexOf(".")) ;
        }

        GMT_ZONE = GMT_ZONE.substring( GMT_ZONE.indexOf(".")+1 );
        if ( GMT_ZONE.equals("0") ){
            GMT_ZONE_FORMATTED += ":00";
        }else{
            GMT_ZONE_FORMATTED += ":30";
        }

        return GMT_ZONE_FORMATTED;
    }

}
