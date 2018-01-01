package io.github.halcyon_daze.TranslinkTracker;

import java.util.ArrayList;
import java.util.Date;

public class BusStop{

    private String stopNo;
    private String routeNo;
    private String routeName;
    private String direction;
    private ArrayList<String> nextDepartureTimes;
    private Date lastUpdated;
    
    public BusStop(String stopNo, String routeNo, String routeName, String direction, ArrayList<String> departureTimes) {
        this.stopNo = stopNo;
        this.direction = direction;
        this.routeNo = routeNo;
        this.routeName = routeName;
        nextDepartureTimes = departureTimes;
        lastUpdated = new Date();
    }

    public String getStopNo() {
        return stopNo;
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
    
    public Date getLastUpdated() {
        return lastUpdated;   
    }
    
    public void printTimes() {
        int i = 0;
        for(String s: nextDepartureTimes) {
            System.out.println("Bus #" + (++i) + " coming at " + s);
        }
    }
}
