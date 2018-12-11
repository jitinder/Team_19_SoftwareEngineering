package com.trafficmon;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VehicleTest {
    public final String registration = "B628 3XQ";
    long timestamp;
    Vehicle testVehicle = new Vehicle(registration);

    @Test
    public void withRegistrationTest() {
        assertEquals(testVehicle.withRegistration(registration), new Vehicle(registration));
    }

    @Test
    public void toStringTest() {
        assertEquals(testVehicle.toString(), "Vehicle [B628 3XQ]");
    }

    @Test
    public void equalsTest() {
        Vehicle v = new Vehicle(registration);
        EntryEvent ee = new EntryEvent(testVehicle, timestamp);

        //test for different vehicles
        String diff_reg = "LT16 R76";
        assertFalse(testVehicle.equals(new Vehicle(diff_reg)));

         // test for same vehicle but different class
        assertFalse(testVehicle.equals(ee));

        //test for same vehicles
        try {
            assertTrue(testVehicle.equals(v));
        }catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Object belongs to a class which is not acceptable");
        }
    }
}
