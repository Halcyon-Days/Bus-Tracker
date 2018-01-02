package com.example.chris.bustracker;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.github.halcyon_daze.TranslinkTracker.BusStop;

/**
 * Created by Chris on 2018-01-01.
 */

/*
    Singleton used to access list of busses in different activities
    Also has a method to add busstops to the list concurrently, but the list remains exposed, so concurrency issues still apply
 */
public final class Singleton {
    private static final Singleton SELF = new Singleton();

    private ArrayList<BusStop> stopList = new ArrayList<BusStop>();
    private Lock listLock = new ReentrantLock();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return SELF;
    }

    public ArrayList<BusStop> getStopList() {
        return stopList;
    }

    public boolean isStopInList(String stopNo) {
        for(BusStop s: stopList) {
            if(s.getStopNo().equals(stopNo)) {
                return true;
            }
        }

        return false;
    }

    public void addStop(BusStop s) {
        listLock.lock();
        stopList.add(s);
        listLock.unlock();
    }

    public void removeStop(BusStop s) {
        listLock.lock();
        stopList.remove(s);
        listLock.unlock();
    }

    public void changeStopList(ArrayList<BusStop> s) {
        listLock.lock();
        this.stopList = s;
        listLock.unlock();
    }
}
