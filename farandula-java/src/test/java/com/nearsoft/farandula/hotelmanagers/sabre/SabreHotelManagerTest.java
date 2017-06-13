package com.nearsoft.farandula.hotelmanagers.sabre;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.hotelmanagers.HotelManager;
import com.nearsoft.farandula.models.*;
import org.junit.jupiter.api.Test;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by pruiz on 6/12/17.
 */
class SabreHotelManagerTest {

    @Test
    void getAvail() throws FarandulaException, IOException {


        List<Object> hotels = Luisa
                .using(createSabreStub())
                .findMeHotels()
                .checkhIn( LocalDateTime.now() )
                .checkOut( LocalDateTime.now().plusDays(3) )
                .forGuest(GuestType.ADULTS , 2)
                .rooms(1)
                .execute();

        //todo assert for null and to cjeck hotels content


    }

    private SabreHotelManager createSabreStub() throws IOException {

        SabreHotelManager supplierStub = new SabreHotelManager() {

            @Override
            public SOAPMessage sendRequest(SOAPMessage message) throws SOAPException {
                InputStream inputStream = this.getClass().getResourceAsStream("/sabre/response/hotels/Example1_PHX.xml");
                SOAPMessage response = null;
                try {
                    response = MessageFactory.newInstance().createMessage(null, inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

        };
        return supplierStub;

    }

}