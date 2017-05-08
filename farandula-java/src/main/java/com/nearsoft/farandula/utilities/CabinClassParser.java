package com.nearsoft.farandula.utilities;

import com.nearsoft.farandula.models.CabinClassType;

/**
 * Created by pruiz on 5/8/17.
 */
public class CabinClassParser {

    public static CabinClassType getCabinClassType(String classCabin) {

        classCabin = classCabin.toLowerCase().trim();
        switch ( classCabin ){
            case "economy" :
                return CabinClassType.ECONOMY;
            case "business" :
                return CabinClassType.BUSINESS;
            case "premiumeconomy":
                return CabinClassType.PREMIUM_ECONOMY;
            case "first":
                return CabinClassType.FIRST;
            default:
                return CabinClassType.OTHER;
        }

    }
}
