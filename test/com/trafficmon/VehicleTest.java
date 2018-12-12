package com.trafficmon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VehicleTest {

    private String registration;
    private long timeStamp;
    private Vehicle testVehicle;

    @Before
    public void setup(){
        registration = "B628 3XQ";
        testVehicle = new Vehicle(registration);
        timeStamp = 0;
    }

    @Test
    public void withRegistrationTest() {
        assertEquals(testVehicle, Vehicle.withRegistration(registration));
    }

    @Test
    public void toStringTest() {
        assertEquals(testVehicle.toString(), "Vehicle [B628 3XQ]");
    }

    @Test
    public void equalsTestDifferentRegistrationNumbers() {
        String diff_reg = "LT16 R76";
        assertFalse(testVehicle.equals(new Vehicle(diff_reg)));
    }

    @Test
    public void equalsTestDifferentClass(){
        EntryEvent ee = new EntryEvent(testVehicle, timeStamp);
        assertFalse(testVehicle.equals(ee));
    }

    @Test
    public void equalsTestSameRegistrationSameClass(){
        Vehicle v = new Vehicle(registration);
        assertTrue(testVehicle.equals(v));
    }

    @After
    public void reset(){
        registration = "";
        testVehicle = null;
    }
}
