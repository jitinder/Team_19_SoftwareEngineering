package com.trafficmon;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VehicleTest {
    public final String registration = "B628 3XQ";
    public final String invalid_reg = "";
    long timestamp;
    Vehicle testVehicle = new Vehicle(registration);

   // String pattern = "(.*)

    // Create pattern object
   // Pattern pattern = Pattern.compile(pattern);

    @Test
    public void withRegistrationTest() {
        assertEquals(testVehicle.withRegistration(registration), new Vehicle(registration));
    }

    @Test
    public void toStringTest() {
        assertEquals(testVehicle.toString(), "Vehicle [B628 3XQ]");
    }

    @Test
    public void equals() {
        Vehicle v = new Vehicle(registration);
        assertTrue(testVehicle.equals(v));
        EntryEvent ee = new EntryEvent(testVehicle, timestamp);

        // test for diff vehicles
        String diff_reg = "LT16 R76";
        assertFalse(testVehicle.equals(new Vehicle(diff_reg)));

        // test for same vehicle but different class
        assertFalse(testVehicle.equals(ee));

        if (ee == null || testVehicle.getClass() != ((Object)ee).getClass()) {
            try {
                Vehicle vehicle = (Vehicle) ((Object)ee.getVehicle());
                assertFalse(testVehicle.equals(ee));
            }catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("Object belongs to a class which is not acceptable");
            }
        }
    }
/*
    @Test
    public void hashCode() {
    }*/
}