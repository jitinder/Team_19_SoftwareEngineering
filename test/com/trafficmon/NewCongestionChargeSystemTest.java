package com.trafficmon;

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

    /*String registration = "45676382";
    Vehicle vehicle;
    NewCongestionChargeSystem newCongestionChargeSystem = new NewCongestionChargeSystem();

    public NewCongestionChargeSystemTest() {
        vehicle = new Vehicle(registration);
    }

    @Test
    public void checkingIfVehicleEnteredTheZone() {
        int preSize = newCongestionChargeSystem.eventLog.size();
        assertEquals(newCongestionChargeSystem.eventLog.size(), preSize);
       newCongestionChargeSystem.vehicleEnteringZone(vehicle);
        assertEquals(newCongestionChargeSystem.eventLog.size(), preSize+1);
    }

    @Test
    public void checkingIfVehicleLeftTheZone() {
        if (!newCongestionChargeSystem.previouslyRegistered(vehicle)) {
            assertNull(newCongestionChargeSystem.vehicleLeavingZone(vehicle));
        }
        assertEquals(newCongestionChargeSystem.eventLog.add(new ExitEvent(vehicle)));
    }



    @Test
    public void getCrossingsPerVehicleTest() {

        HashMap<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = new HashMap<>();
        int previousSize = crossingsPerVehicle.size();

        assertNotNull(newCongestionChargeSystem.getCrossingsPerVehicle());

        for(ZoneBoundaryCrossing crossing: newCongestionChargeSystem.eventLog){
            Vehicle currentVehicle = crossing.getVehicle();
            if(!crossingsPerVehicle.containsKey(currentVehicle)){
                assertTrue(newCongestionChargeSystem.previouslyRegistered(currentVehicle));
            }
            assertEquals(previousSize +1,crossingsPerVehicle.size());
        }
    }*/
}