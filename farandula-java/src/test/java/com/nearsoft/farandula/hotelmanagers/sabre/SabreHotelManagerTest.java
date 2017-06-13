package com.nearsoft.farandula.hotelmanagers.sabre;

import org.junit.jupiter.api.Test;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pruiz on 6/12/17.
 */
class SabreHotelManagerTest {
    @Test
    void getAvail() {
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