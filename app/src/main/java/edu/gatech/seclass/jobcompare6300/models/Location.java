package edu.gatech.seclass.jobcompare6300.models;

public class Location {

    public String city;
    public String state;
    public int costOfLivingIndex;

    public Location(String city, String state, int costOfLivingIndex){
        this.city = city;
        this.state = state;
        this.costOfLivingIndex = costOfLivingIndex;
    }
}
