package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 5/2/17.
 */
public class TravelportFlightDetails {

    private String key;
    private String originalTerminal;
    private String destinationTerminal;
    private Long flightTime;
    private String equipment;
    private String group;

    public void setKey(String key) {
        this.key = key;
    }

    public void setOriginalTerminal(String originalTerminal) {
        this.originalTerminal = originalTerminal;
    }

    public void setDestinationTerminal(String destinationTerminal) {
        this.destinationTerminal = destinationTerminal;
    }

    public void setFlightTime(Long flightTime) {
        this.flightTime = flightTime;
    }
    
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getOriginalTerminal() {
        return originalTerminal;
    }

    public String getDestinationTerminal() {
        return destinationTerminal;
    }

    public Long getFlightTime() {
        return flightTime;
    }

    public String getKey() {
        return key;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
