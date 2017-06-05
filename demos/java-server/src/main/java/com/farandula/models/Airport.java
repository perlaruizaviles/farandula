package com.farandula.models;

import org.springframework.data.annotation.Id;

/**
 * Created by enrique on 1/05/17.
 */
public class Airport implements Cloneable {

    @Id private String _id;

    private String name;
    private String city;
    private String country;
    private String iata;

    public Airport(String name, String city, String country, String iata){
        this.setName(name);
        this.setCity(city);
        this.setCountry(country);
        this.setIata(iata);
    }

    public Object clone(){

        try {

            return super.clone();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;

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


}