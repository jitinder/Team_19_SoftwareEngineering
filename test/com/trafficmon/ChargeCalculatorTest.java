package com.trafficmon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class ChargeCalculatorTest {

    private CongestionChargeCalculator chargeCalculator;
    private Calendar calendar;
    private long currentTimeInMs;
    private String registration;
    private Vehicle vehicle;

    @Before
    public void setup(){
        chargeCalculator = new CongestionChargeCalculator();
        calendar = Calendar.getInstance();
        currentTimeInMs = calendar.getTimeInMillis();
        registration = "BCT1 098";
        vehicle = new Vehicle(registration);
    }

    @Test
    public void canChargeAgainTestLessThanFourHours() {
        boolean result = chargeCalculator.canChargeAgain(currentTimeInMs, currentTimeInMs + (chargeCalculator.ONE_HOUR_IN_MS * 2));
        assertFalse(result);
    }

    @Test
    public void canChargeAgainTestExactlyFourHours(){
        boolean result = chargeCalculator.canChargeAgain(currentTimeInMs, currentTimeInMs + (chargeCalculator.ONE_HOUR_IN_MS * 4));
        assertTrue(result);
    }

    @Test
    public void canChargeAgainTestMoreThanFourHours() {
        boolean result = chargeCalculator.canChargeAgain(currentTimeInMs, currentTimeInMs + (chargeCalculator.ONE_HOUR_IN_MS * 6));
        assertTrue(result);
    }


    @Test
    public void getChargeByTimeTestBeforeTwoPM() {
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        assertEquals(chargeCalculator.getChargeByTime(calendar.getTime().getTime()), chargeCalculator.CHARGE_BEFORE_TWO_PM);
    }

    @Test
    public void getChargeByTimeTestAfterTwoPM() {
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);
        assertEquals(chargeCalculator.getChargeByTime(calendar.getTime().getTime()), chargeCalculator.CHARGE_AFTER_TWO_PM);
    }

    @Test
    public void getChargeByTimeTestAtTwoPM() {
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        assertEquals(chargeCalculator.getChargeByTime(calendar.getTime().getTime()), chargeCalculator.CHARGE_AFTER_TWO_PM);
    }

    @After
    public void reset(){
        chargeCalculator = null;
        calendar = null;
        currentTimeInMs = 0;
        registration = "";
        vehicle = null;
    }
}