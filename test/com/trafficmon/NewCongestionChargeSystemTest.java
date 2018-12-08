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

    List<ZoneBoundaryCrossing> eventLog = new ArrayList<>();

    String registration = "45676382";
    Vehicle vehicle;

    public NewCongestionChargeSystemTest() {
        vehicle = new Vehicle(registration);
    }

    @Test
    public void checkingIfVehicleEnteredTheZone() {
        assertEquals(eventLog.size(), 0);
        NewCongestionChargeSystem.vehicleEnteringZone(vehicle);
        assertEquals(eventLog.size(), 1);
    }

    @Test
    public void vehicleLeavingZoneTest() {
        assertNotNull(ZoneBoundaryCrossing.getVehicle());
    }

    @Test
    public void getCrossingsPerVehicleTest() {

        HashMap<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = new HashMap<>();
        int previousSize = crossingsPerVehicle.size();

        assertNotNull(NewCongestionChargeSystem.getCrossingsPerVehicle());

        for(ZoneBoundaryCrossing crossing: eventLog){
            Vehicle currentVehicle = crossing.getVehicle();
            if(!crossingsPerVehicle.containsKey(currentVehicle)){
                assertTrue(NewCongestionChargeSystem.previouslyRegistered(currentVehicle));
            }
            assertEquals(previousSize +1,crossingsPerVehicle.size());
        }
    }
}