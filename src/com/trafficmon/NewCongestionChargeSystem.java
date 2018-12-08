package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;

public class NewCongestionChargeSystem {

    /* Constants */
    public static final BigDecimal CHARGE_BEFORE_TWO_PM = new BigDecimal(6);
    public static final BigDecimal CHARGE_AFTER_TWO_PM = new BigDecimal(4);
    public static final BigDecimal CHARGE_OVERTIME = new BigDecimal(12);
    public static final int ONE_HOUR_IN_MS = 60 * 60 * 1000;
    public static final int FOUR_HOURS_IN_MINS = 4 * 60;

    private final List<ZoneBoundaryCrossing> eventLog = new ArrayList<ZoneBoundaryCrossing>();

    /* Vehicle Data Methods */

    /**
     * Marks the Vehicle's entry in the zone by storing a {@link EntryEvent} for the Vehicle in the eventLog List
     *
     * @param vehicle is a {@link Vehicle} object
     */
    public void vehicleEnteringZone(Vehicle vehicle) {
        eventLog.add(new EntryEvent(vehicle));
    }

    /**
     * Marks the Vehicle's exit from the zone by storing a {@link ExitEvent} for the Vehicle in the eventLog List
     * If any previous Vehicle event is not yet registered in the system, doesn't add the {@link ExitEvent}.
     *
     * @param vehicle is a {@link Vehicle} object
     */
    public void vehicleLeavingZone(Vehicle vehicle) {
        if (!previouslyRegistered(vehicle)) {
            return;
        }
        eventLog.add(new ExitEvent(vehicle));
    }

    /**
     * Checks whether a previous EventEntry exists in eventLog for the Vehicle entered
     *
     * @param vehicle is a {@link Vehicle} object
     * @return true if a {@link ZoneBoundaryCrossing} event has been previously registered for the event
     * @return false if a {@link ZoneBoundaryCrossing} event has not been previously registered for the event
     */
    public boolean previouslyRegistered(Vehicle vehicle) {
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

    /* Charge Calculation Methods */
    private int minutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.abs(Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0)));
    }

    private boolean canChargeAgain(long startTimeMs, long currentTime) {
        int timeTaken = minutesBetween(startTimeMs, currentTime);
        if(timeTaken <= FOUR_HOURS_IN_MINS){
            return false;
        }
        return true;
    }


    // For Testing
    public static void main(String args[]){
        NewCongestionChargeSystem congestionChargeSystem = new NewCongestionChargeSystem();

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(Vehicle.withRegistration("A123 XYZ"));
        vehicles.add(Vehicle.withRegistration("J091 4PY"));
        vehicles.add(Vehicle.withRegistration("B246 XYZ"));
        vehicles.add(Vehicle.withRegistration("C783 4TT"));
        vehicles.add(Vehicle.withRegistration("D243 5PR"));

        long sysTime = System.currentTimeMillis();
        congestionChargeSystem.eventLog.add(new EntryEvent(vehicles.get(0), sysTime));
        congestionChargeSystem.eventLog.add(new ExitEvent(vehicles.get(0), sysTime + ONE_HOUR_IN_MS));
        congestionChargeSystem.eventLog.add(new EntryEvent(vehicles.get(1), sysTime + ONE_HOUR_IN_MS));
        congestionChargeSystem.eventLog.add(new ExitEvent(vehicles.get(1), sysTime + (3*ONE_HOUR_IN_MS)));

        Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = congestionChargeSystem.getCrossingsPerVehicle();
        for(int i = 0; i < crossingsPerVehicle.size(); i++) {
            Vehicle v = (Vehicle) crossingsPerVehicle.keySet().toArray()[i];
            System.out.print(v + " : ");
            for(int j = 0; j < crossingsPerVehicle.get(v).size(); j++){
                System.out.print(crossingsPerVehicle.get(v).get(j).toString() + " || ");
            }
            System.out.println();
        }
    }

}
