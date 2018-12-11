package com.trafficmon;

import org.junit.Test;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.junit.Assert.*;

public class ChargeCalculatorTest {

    /* Constants */
    private static final BigDecimal CHARGE_BEFORE_TWO_PM = new BigDecimal(6);
    private static final BigDecimal CHARGE_AFTER_TWO_PM = new BigDecimal(4);
    public final BigDecimal CHARGE_OVERTIME = new BigDecimal(12);
    public static final int ONE_HOUR_IN_MS = 60 * 60 * 1000;
    public static final double TWO_PM_IN_MS = 14 * ONE_HOUR_IN_MS;
    public static final double FOUR_PM_IN_MS= 16 * ONE_HOUR_IN_MS;
    private static final int FOUR_HOURS_IN_MINS = 4 * 60;

    ChargeCalculator chargeCalculator = new ChargeCalculator();

    Calendar c = Calendar.getInstance();
    long time = c.getTime().getTime();

    String registration = "BCT1 098";

    Vehicle vehicle = new Vehicle(registration);

    EntryEvent entryEvent = new EntryEvent(vehicle);
    ExitEvent exitEvent = new ExitEvent(vehicle);


    @Test
    public void canChargeAgainTest_returnsFalse() {
        boolean result = chargeCalculator.canChargeAgain(time - (230 * 60 * 1000),time);
        assertFalse(result);
    }

    @Test
    public void canChargeAgainTest_returnsTrue() {
        boolean result = chargeCalculator.canChargeAgain(time, time + (260 * 60 *1000));
        assertTrue(result);
    }

    @Test
    public void getChargeByTimeTest_BeforeTwoPM() {
        Calendar calendarForTwoPm = Calendar.getInstance();
        Calendar calendarForTimestamp = Calendar.getInstance();
        calendarForTwoPm.set(Calendar.HOUR_OF_DAY, 14);
        calendarForTwoPm.set(Calendar.MINUTE, 0);
        calendarForTimestamp.setTimeInMillis(entryEvent.timestamp());

        if (calendarForTwoPm.getTime().getTime()< TWO_PM_IN_MS) {
            assertTrue(chargeCalculator.getChargeByTime(entryEvent.timestamp()) == CHARGE_BEFORE_TWO_PM);
        }
    }

    @Test
    public void getChargeByTimeTest_AfterFourPM() {
        Calendar calendarForFourPm = Calendar.getInstance();
        Calendar calendarForTimestamp = Calendar.getInstance();
        calendarForFourPm.set(Calendar.HOUR_OF_DAY, 16);
        calendarForFourPm.set(Calendar.MINUTE, 5);
        calendarForTimestamp.setTimeInMillis(entryEvent.timestamp());

        if (calendarForFourPm.getTime().getTime()>FOUR_PM_IN_MS) {
            assertTrue(chargeCalculator.getChargeByTime(exitEvent.timestamp()) == CHARGE_OVERTIME);
        }

    }






}