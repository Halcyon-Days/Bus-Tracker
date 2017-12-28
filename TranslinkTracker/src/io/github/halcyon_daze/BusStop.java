package io.github.halcyon_daze;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class BusStop {

    private String routeNo;
    private String routeName;
    private String direction;
    private ArrayList<String> nextDepartureTimes;
    private LocalDateTime lastUpdated;
    
    public BusStop(String routeNo, String routeName, String direction, ArrayList<String> departureTimes) {
        this.direction = direction;
        this.routeNo = routeNo;
        this.routeName = routeName;
        nextDepartureTimes = departureTimes;
        lastUpdated = LocalDateTime.now();
    }
    
    public String getRouteNo() {
        return routeNo;
    }
    
    public String getRouteName(){
        return routeName;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public ArrayList<String> getNextDepartureTimes() {
        return new ArrayList<String>(nextDepartureTimes);
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;   
    }
    
    public void printTimes() {
        int i = 0;
        for(String s: nextDepartureTimes) {
            System.out.println("Bus #" + (++i) + " coming at " + s);
        }
    }
}
