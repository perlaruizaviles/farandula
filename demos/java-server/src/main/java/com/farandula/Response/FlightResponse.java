package com.farandula.Response;



import com.farandula.models.Flight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoniohernandez on 5/6/17.
 */
public class FlightResponse {

    private List< List<Flight> > content = new ArrayList<>();


    public List<List<Flight>> getContent(){return this.content;}



    public void setContent(List<Flight> depart, List<Flight> arrival){
        this.content.add(depart);
        this.content.add(arrival);
    }

    public void setContent(List<Flight> legs){
        this.content.add(legs);
    }



    public static List<List<Flight>> getResponseInstance(List<Flight> departLegs, List<Flight> returnLegs){
        List<List<Flight>> response  = new ArrayList<>();
        response.add(departLegs);
        response.add(returnLegs);
        return response;
    }

    public static List<List<Flight>> getResponseInstance( List<Flight> departLegs){
        List<List<Flight>> response  = new ArrayList<>();
        response.add(departLegs);
        return response;
    }
}
