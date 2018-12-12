package com.trafficmon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class CongestionChargeCalculatorTest {

    private CongestionChargeCalculator chargeCalculator;
    private final long HALF_HOUR_IN_MS = 30*60*1000;


    private EventLog mockEventLog;
    private Calendar calendarForTimestamp;
    private List<Vehicle> vehicles;

   @Before
    public void setup(){
        chargeCalculator = new CongestionChargeCalculator();
        calendarForTimestamp = Calendar.getInstance();
        mockEventLog = EventLog.getInstance();

        vehicles = new ArrayList<>();
        vehicles.add(Vehicle.withRegistration("A123 8YZ"));
        vehicles.add(Vehicle.withRegistration("J091 4PY"));
        vehicles.add(Vehicle.withRegistration("B246 XYZ"));
        vehicles.add(Vehicle.withRegistration("C783 4TT"));
        vehicles.add(Vehicle.withRegistration("D243 5PR"));
        vehicles.add(Vehicle.withRegistration("C673 7FD"));
        vehicles.add(Vehicle.withRegistration("A110 1HT"));
        vehicles.add(Vehicle.withRegistration("B763 5TZ"));
        vehicles.add(Vehicle.withRegistration("C431 4UH"));
        vehicles.add(Vehicle.withRegistration("B736 7FD"));
        vehicles.add(Vehicle.withRegistration("B222 4GB"));
        vehicles.add(Vehicle.withRegistration("D243 3GH"));
        vehicles.add(Vehicle.withRegistration("B876 9EE"));
        vehicles.add(Vehicle.withRegistration("A236 4WE"));
        vehicles.add(Vehicle.withRegistration("E432 6ZX"));
        vehicles.add(Vehicle.withRegistration("T846 3WW"));
    }

    @Test
    public void getChargedEntryBeforeTwoExitWithinFourHours() {   //v0
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 11);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(0), calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(0), calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*2)));


        assertEquals(new BigDecimal(6), chargeCalculator.getChargeForVehicle(vehicles.get(0)));

    } // 6

    @Test
    public void getChargedEntryAfterTwoExitWithinFourHours() {
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 15);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(1),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(1), calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*3)));

        assertEquals(new BigDecimal(4),chargeCalculator.getChargeForVehicle(vehicles.get(1)));

    } // 4

    @Test
    public void getChargedEntryBeforeTwoExitAfterFourHours(){
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 12);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(2),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(2), calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*5)));


        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(2)));
    } // 12

    @Test
    public void getChargedEntryAfterTwoExitAfterFourHours(){
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 15);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(3),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(3), calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*5)));


        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(3)));

    } //12

    @Test
    public void EntryBeforeTwoExitWithinFourReenterWithinFourExitWithinFour(){ //v4
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 13);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(4),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(4), calendarForTimestamp.getTimeInMillis() + HALF_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(4),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(4), calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*2)));

        assertEquals(new BigDecimal(6),chargeCalculator.getChargeForVehicle(vehicles.get(4)));

    } // 6

    @Test
    public void EntryBeforeTwoExitWithinFourReenterAfterFourExitBeforeTwo(){  //v5

        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 7);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(5),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(5), calendarForTimestamp.getTimeInMillis() + HALF_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(5),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(5), calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*6)));



        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(5)));

    } // 12

    @Test
    public void EntryBeforeTwoExitWithinFourReenterAfterFourExitAfterTwo(){ //v6
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 11);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(6),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(6), calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(6),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(6), calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*6)));



        assertEquals(new BigDecimal(10),chargeCalculator.getChargeForVehicle(vehicles.get(6)));

    } // 10

    @Test
    public void EntryAfterTwoExitWithinFourReenterWithinFourExitWithinFour(){ //v7


        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 15);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(7),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(7), calendarForTimestamp.getTimeInMillis() + HALF_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(7),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(7), calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*2));


        assertEquals(new BigDecimal(4),chargeCalculator.getChargeForVehicle(vehicles.get(7)));

    } // 4

    @Test
    public void EntryAfterTwoExitWithinFourReenterAfterFourExitAfterTwo(){ //v8
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 15);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(8),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(8), calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS));

        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(8),  calendarForTimestamp.getTimeInMillis() + (chargeCalculator.ONE_HOUR_IN_MS*5)));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(8),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*6));

        Map<Vehicle, BigDecimal> charges = chargeCalculator.getChargeList();

        assertEquals(new BigDecimal(8),chargeCalculator.getChargeForVehicle(vehicles.get(8)));
    } // 8

    @Test
    public void EntryBeforeTwoExitWithinFourReenterWithinFourExitAfterFour(){ //V9
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 11);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(9),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(9), calendarForTimestamp.getTimeInMillis() + HALF_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(9),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(9),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));



        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(9)));
    } // 12

   @Test public void EntryAfterTwoExitWithinFourReenterWithinFourExitAfterFour(){ //v10
       calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 15);
       mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(10),  calendarForTimestamp.getTimeInMillis()));
       mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(10), calendarForTimestamp.getTimeInMillis() + HALF_HOUR_IN_MS));
       mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(10),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS));
       mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(10),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));

       assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(10)));

    } // 12

    @Test
    public void EntryBeforeTwoExitAfterFourReenterBeforeTwoExitBeforeFour(){ //v11
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 8);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(11),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(11), calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(11),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5 + HALF_HOUR_IN_MS));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(11),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*6));

        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(11)));

    } // 12

    @Test
    public void EntryBeforeTwoExitAfterFourReenterAfterTwoExitBeforeFour(){ //v12
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 9);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(12),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(12), calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(12),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*6));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(12),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*8));

        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(12)));

    } // 12

    @Test
    public void EntryAfterTwoExitAfterFourReenterAfterTwoExitBeforeFour(){  //v13
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 15);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(13),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(13), calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(13),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*6));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(13),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*8));

        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(13)));
    } // 12

    @Test
    public void EntryBeforeTwoExitAfterFourReenterAfterTwoExitAfterFour(){ //V14
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 11);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(14),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(14), calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*5));
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(14),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*6));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(14),  calendarForTimestamp.getTimeInMillis() + chargeCalculator.ONE_HOUR_IN_MS*10 + HALF_HOUR_IN_MS));



        assertEquals(new BigDecimal(12),chargeCalculator.getChargeForVehicle(vehicles.get(14)));
    } // 12

    @Test
    public void WrongOrderingTest() {
        calendarForTimestamp.set(Calendar.HOUR_OF_DAY, 15);
        mockEventLog.getEventLog().add(new EntryEvent(vehicles.get(15),  calendarForTimestamp.getTimeInMillis()));
        mockEventLog.getEventLog().add(new ExitEvent(vehicles.get(15), calendarForTimestamp.getTimeInMillis() - (chargeCalculator.ONE_HOUR_IN_MS*5)));



        assertEquals(new BigDecimal(0),chargeCalculator.getChargeForVehicle(vehicles.get(15)));
    }

    @After
    public void reset(){
        chargeCalculator = null;
        calendarForTimestamp = null;
    }
}