package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class NewCongestionChargeSystemTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    String registration = "AXH 110";
    Vehicle vehicle;
    long timestamp;

    public NewCongestionChargeSystemTest() {
        vehicle = new Vehicle(registration);
    }

    NewCongestionChargeSystem newCongestionChargeSystem = new NewCongestionChargeSystem();

    EntryEvent entryEvent;


   @Test
    public void vehicleEnteringZoneTest() {
        int previousSize = newCongestionChargeSystem.eventLog.size();
        assertEquals(newCongestionChargeSystem.eventLog.size(), previousSize);
       newCongestionChargeSystem.vehicleEnteringZone(vehicle);
        assertEquals(newCongestionChargeSystem.eventLog.size(), previousSize+1);
    }

    /*

    @Test
    public void vehicleLeavingZoneTest_forNewVehicleEntry() {
        newCongestionChargeSystem.previouslyRegistered(vehicle) = true;
        {
            assertEquals(newCongestionChargeSystem.vehicleLeavingZone(vehicle), );
        }
    }

    public void vehicleLeavingZoneTest_forPreviouslyRegisteredVehicle() {

    }*/


    @Test
    public void previouslyRegisteredTest_forNewVehicles() {
         //
         String newRegistration = "ABC 123";
         Vehicle newVehicle = new Vehicle(newRegistration);

         assertFalse(newCongestionChargeSystem.previouslyRegistered(newVehicle));
    }

   @Test
   public void previouslyRegisteredTest_forRegisteredVehicles() {
         entryEvent = new EntryEvent(vehicle, timestamp);
         newCongestionChargeSystem.eventLog.add(entryEvent);

         assertTrue(newCongestionChargeSystem.previouslyRegistered(vehicle));

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


 /*

    @Test
    public void checkOrderingOf() {
    }

    @Test
    public void main() {
    }
    */

    public void tearDown() {
        newCongestionChargeSystem = null;
    }

}
