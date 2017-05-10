package com.farandula.Response;

import com.farandula.Flight;
import com.nearsoft.farandula.models.AirLeg;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Null;
import java.util.ArrayList;
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
    private List< List<AirLeg> > content = new ArrayList<>();

    public Integer getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public List<List<AirLeg>> getContent(){return this.content;}

    public void setStatus(Integer status){this.status = status;}
    public void setMessage(String message){this.message = message;}

    public void setContent(List<AirLeg> depart, List<AirLeg> arrival){
        this.content.add(depart);
        this.content.add(arrival);
    }

    public void setContent(List<AirLeg> legs){
        this.content.add(legs);
    }

    public void setDepartAirlegs(List<AirLeg> departAirlegs){this.departAirlegs = departAirlegs;}
    public void setReturnAirlegs(List<AirLeg> returnAirlegs){this.returnAirlegs = returnAirlegs;}

    public List<AirLeg> getDepartAirlegs(){return this.departAirlegs;}
    public List<AirLeg> getReturnAirlegs(){return this.returnAirlegs;}

    public static FlightResponse getResponseInstance(Integer status, String message, List<AirLeg> departLegs, List<AirLeg> returnLegs){
        FlightResponse res = new FlightResponse();

        res.setStatus(status);
        res.setMessage(message);

        res.setContent(departLegs, returnLegs);

        return res;
    }

    public static FlightResponse getResponseInstance(Integer status, String message, List<AirLeg> departLegs){
        FlightResponse res = new FlightResponse();

        res.setStatus(status);
        res.setMessage(message);

        res.setContent(departLegs);

        return res;
    }
}
