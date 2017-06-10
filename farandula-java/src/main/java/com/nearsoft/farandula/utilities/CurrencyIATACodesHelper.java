package com.nearsoft.farandula.utilities;

import com.nearsoft.farandula.models.Price;

/**
 * Created by pruiz on 5/14/17.
 */
public class CurrencyIATACodesHelper {

    public static Price buildPrice(String price ){

        String currency = price.replaceAll("[^a-zA-Z]+", "");

        String amount = "";
        if ( "".equals(currency) ){
            currency  = "US";
            amount = price;
        }else{
            amount = price.replace( currency, "");

        }

        Price priceResult = new Price();
        priceResult
                .setAmount( Double.parseDouble( amount ) )
                .setCurrencyCode( currency );

        return priceResult;
    }
}
