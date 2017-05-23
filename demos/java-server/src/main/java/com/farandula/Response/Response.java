package com.farandula.Response;

import com.farandula.models.Airport;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by enrique on 1/05/17.
 */
public class Response {


    private List<Airport> content;


    public List<Airport> getContent(){return this.content;}


    public void setContent(List<Airport> content){this.content = content;}

    public static Response getResponseInstance(List<Airport> content){

        Response res = new Response();
        res.setContent(content);

        return res;
    }


}