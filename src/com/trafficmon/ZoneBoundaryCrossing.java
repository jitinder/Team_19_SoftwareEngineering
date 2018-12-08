package com.trafficmon;

import java.util.Date;

public abstract class ZoneBoundaryCrossing {

    private final Vehicle vehicle;
    private final long time;

    public ZoneBoundaryCrossing(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = System.currentTimeMillis();
    }

    //TEST ONLY
    public ZoneBoundaryCrossing(Vehicle vehicle, long time){
        this.vehicle = vehicle;
        this.time = time;
    }

    @Override
    public String toString() {
        return this.vehicle.toString() + " - " + new Date(time);
    }

    //TEST ONLY ENDS

    public Vehicle getVehicle() {
        return vehicle;
    }

    public long timestamp() {
        return time;
    }
}
