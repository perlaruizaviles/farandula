package com.farandula.Response;

import com.farandula.models.Airport;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by enrique on 1/05/17.
 */
public class Response {

    @Id
    private Integer status;

    private String message;
    private List<Airport> content;

    public Integer getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public List<Airport> getContent(){return this.content;}

    public void setStatus(Integer status){this.status = status;}
    public void setMessage(String message){this.message = message;}
    public void setContent(List<Airport> content){this.content = content;}

    public static Response getResponseInstance(Integer status, String message, List<Airport> content){
        Response res = new Response();

        res.setStatus(status);
        res.setMessage(message);
        res.setContent(content);

        return res;
    }


}