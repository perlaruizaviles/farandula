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

    private String message;
    private List<AirLeg> content;

    public Integer getStatus(){return this.status;}
    public String getMessage(){return this.message;}
    public List<AirLeg> getContent(){return this.content;}

    public void setStatus(Integer status){this.status = status;}
    public void setMessage(String message){this.message = message;}
    public void setContent(List<AirLeg> content){this.content = content;}

    public static FlightResponse getResponseInstance(Integer status, String message, List<AirLeg> content){
        FlightResponse res = new FlightResponse();

        res.setStatus(status);
        res.setMessage(message);
        res.setContent(content);

        return res;
    }
}
