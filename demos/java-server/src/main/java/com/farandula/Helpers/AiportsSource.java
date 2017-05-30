package com.farandula.Helpers;


import com.farandula.models.Airport;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;


/**
 * Created by Admin on 5/29/17.
 */
public class AiportsSource {

    public static Airport getAirport(String key){
        return (Airport) airportCodes.get(key).clone();
    }

    public static int getAirportsCount(){
        return airportCodes.size();
    }

    private static HashMap<String, Airport> airportCodes = parseAirports();
    private static HashMap<String, Airport> parseAirports(){

        HashMap<String, Airport> result = new HashMap<String, Airport>();

        JSONParser parser = new JSONParser();

        Object file = null;

        try{

            Stream<Path> paths = Files.walk(Paths.get("./src/main/resources"));
            paths.filter(Files::isRegularFile).forEach(System.out::println);

            file = parser.parse(FileUtils.readFileToString(new File("./src/main/resources/resultAir.json")));
        } catch (Exception e){
            e.printStackTrace();
        }

        JSONObject object = (JSONObject) file;
        JSONArray airportArray = (JSONArray) object.get("airports");


        for (int i = 0; i < airportArray.size(); i++){

            JSONObject data = (JSONObject) airportArray.get(i);

            String name = (String) data.get("name");
            String city = (String) data.get("city");
            String country = (String) data.get("country");
            String iata = (String) data.get("iata");

            Airport airportData = new Airport(name, city, country, iata);

            result.put(iata, airportData);

        }

        return result;
    }
}


