package com.farandula.models;

import org.springframework.data.annotation.Id;

/**
 * Created by enrique on 1/05/17.
 */
public class Airport {

    @Id private String _id;

    private int id;

    private String name;
    private String city;
    private String country;
    private String iata;
    private String type;

    public Airport(int id, String name, String city, String country, String iata){
        this.setId(id);
        this.setName(name);
        this.setCity(city);
        this.setCountry(country);
        this.setIata(iata);
        this.setType("airport");
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCity(){
        return this.city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getIata(){
        return this.iata;
    }

    public void setIata(String iata){
        this.iata = iata;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }
}