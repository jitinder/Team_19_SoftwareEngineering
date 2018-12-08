package com.trafficmon;

public class EntryEvent extends ZoneBoundaryCrossing {
    public EntryEvent(Vehicle vehicleRegistration) {
        super(vehicleRegistration);
    }

    //TEST ONLY
    public EntryEvent(Vehicle vehicleRegistration, long time) {
        super(vehicleRegistration, time);
    }
}
