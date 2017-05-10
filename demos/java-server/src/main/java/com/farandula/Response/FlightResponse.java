package com.farandula.Response;



import com.farandula.models.Flight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoniohernandez on 5/6/17.
 */
public class FlightResponse {

    private Integer status;



    private String message;
    private List< List<Flight> > content = new ArrayList<>();

    public Integer getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public List<List<Flight>> getContent(){return this.content;}

    public void setStatus(Integer status){this.status = status;}
    public void setMessage(String message){this.message = message;}

    public void setContent(List<Flight> depart, List<Flight> arrival){
        this.content.add(depart);
        this.content.add(arrival);
    }

    public void setContent(List<Flight> legs){
        this.content.add(legs);
    }



    public static FlightResponse getResponseInstance(Integer status, String message, List<Flight> departLegs, List<Flight> returnLegs){
        FlightResponse res = new FlightResponse();

        res.setStatus(status);
        res.setMessage(message);

        res.setContent(departLegs, returnLegs);

        return res;
    }

    public static FlightResponse getResponseInstance(Integer status, String message, List<Flight> departLegs){
        FlightResponse res = new FlightResponse();

        res.setStatus(status);
        res.setMessage(message);

        res.setContent(departLegs);

        return res;
    }
}
