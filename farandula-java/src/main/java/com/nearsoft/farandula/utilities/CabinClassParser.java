package com.nearsoft.farandula.utilities;

import com.nearsoft.farandula.models.CabinClassType;

/**
 * Created by pruiz on 5/8/17.
 */
public class CabinClassParser {

    public static CabinClassType getCabinClassType(String classCabin) {

        classCabin = classCabin.toLowerCase();
        classCabin = classCabin.replace("class", "");
        classCabin = classCabin.trim();
        switch ( classCabin ){
            case "economy" :
                return CabinClassType.ECONOMY;
            case "business" :
                return CabinClassType.BUSINESS;
            case "premiumeconomy":
                return CabinClassType.PREMIUM_ECONOMY;
            case "first":
                return CabinClassType.FIRST;
            case "economy/coach":
                return CabinClassType.ECONOMYCOACH;
            default:
                return CabinClassType.OTHER;
        }

    }
}
