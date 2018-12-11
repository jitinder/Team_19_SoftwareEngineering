package com.trafficmon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventLog {

    /* Variables */
    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();
    private static EventLog instance = new EventLog();

    /* Getters and Setters */
    /**
     * Instantiates the {@link EventLog} at runtime
     *
     * @return the instance of this class
     */
    public static EventLog getInstance(){
        return instance;
    }

    /**
     * Used to access the {@link EventLog} list
     *
     * @return a list of {@link ZoneBoundaryCrossing} that represents all {@link Vehicle} activity in the zone
     */
    public List<ZoneBoundaryCrossing> getEventLog() {
        return eventLog;
    }


    /* EventLog Manipulation Methods */
    /**
     * Marks the {@link Vehicle}'s entry in the zone by storing a {@link EntryEvent} for the {@link Vehicle} in the eventLog List
     *
     * @param vehicle is a {@link Vehicle} object
     */
    public void vehicleEntryEvent(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }

    /**
     * Marks the {@link Vehicle}'s exit from the zone by storing a {@link ExitEvent} for the {@link Vehicle} in the eventLog List
     * If any previous {@link Vehicle} event is not yet registered in the system, doesn't add the {@link ExitEvent}.
     *
     * @param vehicle is a {@link Vehicle} object
     */
    public void vehicleExitEvent(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.add(new ExitEvent(vehicle));
    }

    /* EventLog Logic-Validating Methods */
    /**
     * Checks whether a previous EventEntry exists in eventLog for the Vehicle entered
     *
     * @param vehicle is a {@link Vehicle} object
     * @return true if a {@link ZoneBoundaryCrossing} event has been previously registered for the event
     * @return false if a {@link ZoneBoundaryCrossing} event has not been previously registered for the event
     */
    private boolean previouslyRegistered(Vehicle vehicle) {
        for (ZoneBoundaryCrossing crossing : eventLog) {
            if (crossing.getVehicle().equals(vehicle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the order of the Events added to the List of {@link ZoneBoundaryCrossing}
     *
     * @param crossings is a List of {@link ZoneBoundaryCrossing}
     * @return true if the order of entries is valid
     * @return false if the order of entries is not appropriate
     */
    public boolean checkOrderingOf(List<ZoneBoundaryCrossing> crossings) {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {
            if (crossing.timestamp() < lastEvent.timestamp()) {
                return false;
            }
            if (crossing instanceof EntryEvent && lastEvent instanceof EntryEvent) {
                return false;
            }
            if (crossing instanceof ExitEvent && lastEvent instanceof ExitEvent) {
                return false;
            }
            lastEvent = crossing;
        }

        return true;
    }

    /* EventLog Cleaning and Sorting Methods */
    /**
     * Makes a Map of crossings per Vehicle from the eventLog
     *
     * @return a Map of Vehicle to List of all crossings in the eventLog per day.
     */
    public Map<Vehicle, List<ZoneBoundaryCrossing>> getCrossingsPerVehicle(){
        HashMap<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = new HashMap<>();

        for(ZoneBoundaryCrossing crossing: eventLog){
            Vehicle currentVehicle = crossing.getVehicle();
            if(!crossingsPerVehicle.containsKey(currentVehicle)){
                crossingsPerVehicle.put(currentVehicle, new ArrayList<ZoneBoundaryCrossing>());
            }
            crossingsPerVehicle.get(currentVehicle).add(crossing);
        }

        return crossingsPerVehicle;
    }

}
