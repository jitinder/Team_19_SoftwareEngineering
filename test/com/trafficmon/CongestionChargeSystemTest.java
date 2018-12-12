package com.trafficmon;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
import java.math.BigDecimal;
import java.util.*;
import java.time.*;


import static org.junit.Assert.*;

public class CongestionChargeSystemTest {
/*

    private static final BigDecimal CHARGE_BEFORE_TWO_PM = new BigDecimal(6);
    private static final BigDecimal CHARGE_AFTER_TWO_PM = new BigDecimal(4);
    private static final BigDecimal CHARGE_OVERTIME = new BigDecimal(12);

    public static final int ONE_HOUR_IN_MS = 60 * 60 * 1000;
    public static final int FOUR_HOURS_IN_MINS = 4 * 60;


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    AccountsService mockAccountsService = context.mock(AccountsService.class);
    PenaltiesService mockPenaltiesService = context.mock(PenaltiesService.class);

    public void setMockAccountsService( AccountsService mockAccountsService ) {
        this.mockAccountsService = RegisteredCustomerAccountsService.getInstance();
    }

    public void setMockPenaltiesService (PenaltiesService mockPenaltiesService) {
        this.mockPenaltiesService = OperationsTeam.getInstance();
    }

    CongestionChargeSystem newCongestionChargeSystem = CongestionChargeSystem.getInstance();

    String registration = "HT1A 110";
    Vehicle vehicle;

    public CongestionChargeSystemTest() {
        vehicle = new Vehicle(registration);
    }

    //for getChargeList Tests
    Calendar current = Calendar.getInstance();

    */
/**
     * Checks the size of the eventLog before and after the vehicleEnteringZone method is called.
     * If the size of eventLog increments by 1 after method has been called, it means method is working
     * and the test passes.
     *//*

    @Test
    public void vehicleEnteringZoneTest() {
        int previousSize = newCongestionChargeSystem.getEventLog().size();
        assertEquals(newCongestionChargeSystem.getEventLog().size(),previousSize);
        newCongestionChargeSystem.vehicleEnteringZone(vehicle);
        assertEquals(newCongestionChargeSystem.getEventLog().size(),previousSize + 1);
    }

    */
/**
     * Tests if eventLog size increases after the method is called.
     * This is because if the vehicle has been previously registered, leaving the zone would add an exit event
     * to the eventLog, thereby incrementing its size by 1.
     *
     *//*

    @Test
    public void vehicleLeavingZoneTest_forPreviouslyRegisteredVehicles() {
        int previousSize = newCongestionChargeSystem.getEventLog().size();

        newCongestionChargeSystem.vehicleEnteringZone(vehicle);
        previousSize += 1;
        assertEquals(newCongestionChargeSystem.getEventLog().size(),previousSize);

        newCongestionChargeSystem.vehicleLeavingZone(vehicle);
        assertEquals(newCongestionChargeSystem.getEventLog().size(),previousSize + 1);
    }

    */
/**
     * Tests the method for vehicles that have not been previously registered.
     * If they are new Vehicles then calling the vehicleLeavingZone method will not do anything.
     * Hence, test checls for increment in eventLog size (should be none in this case)
     *//*

    @Test
    public void vehicleLeavingZoneTest_forNewVehicles() {
        String registration = "BCT1 089";
        Vehicle newVehicle = new Vehicle(registration);

        int previousSize = newCongestionChargeSystem.getEventLog().size();
        assertEquals(newCongestionChargeSystem.getEventLog().size(),previousSize);

        newCongestionChargeSystem.vehicleLeavingZone(newVehicle);
        assertNotEquals(newCongestionChargeSystem.getEventLog().size(),previousSize + 1);
    }

    */
/*
    @Test
    public void checkOrderingOfTest() {

        ZoneBoundaryCrossing lastEvent = crossings.get(0);

        assertFalse(newCongestionChargeSystem.checkOrderingOf(crossings));
       // assertTrue(lastEvent== crossing);
    }
*//*

    @Test
    public void getCrossingsPerVehicleTest() {
        HashMap<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = new HashMap<>();
        int previousSize = crossingsPerVehicle.size();

        assertNotNull(newCongestionChargeSystem.getEventLog());

        for (ZoneBoundaryCrossing crossing : newCongestionChargeSystem.getEventLog()) {
            Vehicle currentVehicle = crossing.getVehicle();
            if (!crossingsPerVehicle.containsKey(currentVehicle)) {
                assertNotEquals(crossingsPerVehicle.size(),previousSize + 1);
            }
        }
        assertEquals(crossingsPerVehicle.size(),previousSize);
    }

    public List<ZoneBoundaryCrossing> create_mock_eventLog() {
        //A Duration can have a negative value, if it is created with an end point that occurs before the start point.

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(Vehicle.withRegistration("A123 XYZ"));
        vehicles.add(Vehicle.withRegistration("J091 4PY"));
        vehicles.add(Vehicle.withRegistration("B246 XYZ"));
        vehicles.add(Vehicle.withRegistration("C783 4TT"));
        vehicles.add(Vehicle.withRegistration("D243 5PR"));

        Calendar c = Calendar.getInstance();

        long time = c.getTime().getTime();

        EventLog mock_eventLog = EventLog.getInstance();

        // vehicle enters before 2 pm
        mock_eventLog.vehicleEntryEvent(vehicles.get(0));
        mock_eventLog.vehicleExitEvent(vehicles.get(0));

        mock_eventLog.vehicleEntryEvent(vehicles.get(1));
        mock_eventLog.vehicleEntryEvent(vehicles.get(1));
        mock_eventLog.vehicleEntryEvent(vehicles.get(1));
        mock_eventLog.vehicleExitEvent(vehicles.get(1));

        mock_eventLog.add(new EntryEvent(vehicles.get(2), time));
        mock_eventLog.add(new ExitEvent(vehicles.get(2), time + (6*ONE_HOUR_IN_MS)));




        return mock_eventLog;

    }

    public void tearDown() {
        newCongestionChargeSystem = null;
    }
*/


}