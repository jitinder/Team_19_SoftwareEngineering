package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.IdentityExpectationErrorTranslator;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NewCongestionChargeSystemTest {

    public static final int ONE_HOUR_IN_MS = 60 * 60 * 1000;
    public static final int FOUR_HOURS_IN_MINS = 4 * 60;
    public static final double DOUBLE = 1000.0 * 60.0;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    String registration = "AXH 110";
    Vehicle vehicle;
    long timestamp;

    public NewCongestionChargeSystemTest() {
        vehicle = new Vehicle(registration);
    }

    NewCongestionChargeSystem newCongestionChargeSystem = new NewCongestionChargeSystem();

    ExitEvent exitEvent;

    int previousSize = newCongestionChargeSystem.eventLog.size();

   @Test
    public void vehicleEnteringZoneTest() {
        assertEquals(newCongestionChargeSystem.eventLog.size(), previousSize);
       newCongestionChargeSystem.vehicleEnteringZone(vehicle);
        assertEquals(newCongestionChargeSystem.eventLog.size(), previousSize+1);
    }

    @Test
    public void vehicleLeavingZoneTest() {
       if (!newCongestionChargeSystem.previouslyRegistered(vehicle)) {
           previouslyRegisteredTest_forNewVehicles();
       }
       previouslyRegisteredTest_forRegisteredVehicles();
    }

    public void previouslyRegisteredTest_forNewVehicles() {
         String newRegistration = "ABC 123";
         Vehicle newVehicle = new Vehicle(newRegistration);

         assertFalse(newCongestionChargeSystem.previouslyRegistered(newVehicle));
    }

    public void previouslyRegisteredTest_forRegisteredVehicles() {
         exitEvent = new ExitEvent(vehicle, timestamp);
         newCongestionChargeSystem.eventLog.add(exitEvent);

         assertTrue(newCongestionChargeSystem.previouslyRegistered(vehicle));
    }
/*
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
*/

    @Test
    public void checkOrderingOfTest() {

        List<ZoneBoundaryCrossing> crossings = new ArrayList<>();
        crossings.add(0, new EntryEvent(vehicle, 783641));
        crossings.add(1, new EntryEvent(vehicle, 534688));
        crossings.add(2, new ExitEvent(vehicle, 87346));
        crossings.add(3, new ExitEvent(vehicle, 343232765));

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        assertFalse(newCongestionChargeSystem.checkOrderingOf(crossings));
       // assertTrue(lastEvent== crossing);
    }


    @Test
    public void getCrossingsPerVehicleTest(){
        HashMap<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = new HashMap<>();
        int previousSize = crossingsPerVehicle.size();

        assertNotNull(newCongestionChargeSystem.getCrossingsPerVehicle());

        for(ZoneBoundaryCrossing crossing: newCongestionChargeSystem.eventLog){
            Vehicle currentVehicle = crossing.getVehicle();
            if(!crossingsPerVehicle.containsKey(currentVehicle)){
                assertTrue(newCongestionChargeSystem.previouslyRegistered(currentVehicle));
            }
            assertEquals(crossingsPerVehicle.size(),previousSize +1);
        }
    }


    @Test
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
        congestionChargeSystem.eventLog.add(new EntryEvent(vehicles.get(0), sysTime + (3*ONE_HOUR_IN_MS)));
        congestionChargeSystem.eventLog.add(new ExitEvent(vehicles.get(0), sysTime + (3*ONE_HOUR_IN_MS)));

        Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = congestionChargeSystem.getCrossingsPerVehicle();
        for(int i = 0; i < crossingsPerVehicle.size(); i++) {
            Vehicle v = (Vehicle) crossingsPerVehicle.keySet().toArray()[i];
            System.out.println(v + " :");
            for(int j = 0; j < crossingsPerVehicle.get(v).size(); j++){
                ZoneBoundaryCrossing zone = crossingsPerVehicle.get(v).get(j);
                String instance = "";
                if(zone instanceof EntryEvent){
                    instance = "Entry";
                } else {
                    instance = "Exit";
                }
                System.out.println(zone.toString() + " " + instance + " || ");
            }
            System.out.println("Order correct? " + congestionChargeSystem.checkOrderingOf(crossingsPerVehicle.get(v)));
        }
    }

    public void tearDown() {
        newCongestionChargeSystem = null;
    }

}
