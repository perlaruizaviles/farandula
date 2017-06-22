package com.nearsoft.farandula.hotelmanagers.sabre;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.hotelmanagers.HotelManager;
import com.nearsoft.farandula.models.HotelSearchCommand;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.List;

/**
 * Created by pruiz on 6/12/17.
 */
public class SabreHotelManager implements HotelManager {

    @Override
    public List<Object> getAvail(HotelSearchCommand search) throws FarandulaException, IOException {
        return null;
    }

    public SOAPMessage sendRequest(SOAPMessage message) throws SOAPException {
        return null;
    }


}
