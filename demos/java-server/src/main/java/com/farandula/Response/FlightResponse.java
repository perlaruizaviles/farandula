package com.farandula.Response;

import com.farandula.Flight;
import com.nearsoft.farandula.models.AirLeg;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by antoniohernandez on 5/6/17.
 */
public class FlightResponse {
    @Id
    private Integer status;

    List<AirLeg> departAirlegs;
    List<AirLeg> returnAirlegs;

    private String message;
    private List< List<AirLeg> > content;

    public Integer getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public List<List<AirLeg>> getContent(){return this.content;}

    public void setStatus(Integer status){this.status = status;}
    public void setMessage(String message){this.message = message;}
    public void setContent(List<AirLeg> depart, List<AirLeg> arrival){


    }

    public void setDepartAirlegs(List<AirLeg> departAirlegs){this.departAirlegs = departAirlegs;}
    public void setReturnAirlegs(List<AirLeg> returnAirlegs){this.returnAirlegs = returnAirlegs;}

    public List<AirLeg> getDepartAirlegs(){return this.departAirlegs;}
    public List<AirLeg> getReturnAirlegs(){return this.returnAirlegs;}

    public static FlightResponse getResponseInstance(Integer status, String message, List<List<AirLeg>> content){
        FlightResponse res = new FlightResponse();

        res.setStatus(status);
        res.setMessage(message);
        res.setContent(content);

        return res;
    }
}
