package com.trafficmon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CongestionChargeCalculatorTest {

    private CongestionChargeCalculator chargeCalculator;

    @Before
    public void setup(){
        chargeCalculator = new CongestionChargeCalculator();
    }

    @Test
    public void getChargeList() {
    }

    @Test
    public void getChargedEntryBeforeTwoExitWithinFourHours(){} // 6
    public void getChargedEntryAfterTwoExitWithinFourHours(){} // 4
    public void getChargedEntryBeforeTwoExitAfterFourHours(){} // 12
    public void getChargedEntryAfterTwoExitAfterFourHours(){} //12
    public void EntryBeforeTwoExitWithinFourReenterWithinFourExitWithinFour(){} // 6
    public void EntryBeforeTwoExitWithinFourReenterAfterFourExitBeforeTwo(){} // 12
    public void EntryBeforeTwoExitWithinFourReenterAfterFourExitAfterTwo(){} // 10
    public void EntryAfterTwoExitWithinFourReenterWithinFourExitWithinFour(){} // 4
    public void EntryAfterTwoExitWithinFourReenterAfterFourExitAfterTwo(){} // 8

    public void EntryBeforeTwoExitWithinFourReenterWithinFourExitAfterFour(){} // 12
    public void EntryAfterTwoExitWithinFourReenterWithinFourExitAfterFour(){} // 12

    public void EntryBeforeTwoExitAfterFourReenterBeforeTwoExitBeforeFour(){} // 12
    public void EntryBeforeTwoExitAfterFourReenterAfterTwoExitBeforeFour(){} // 12
    public void EntryAfterTwoExitAfterFourReenterAfterTwoExitBeforeFour(){} // 12

    public void EntryBeforeTwoExitAfterFourReenterAfterTwoExitAfterFour(){} // 12

    @Test
    public void getChargeForVehicle() {
    }

    @After
    public void reset(){
        chargeCalculator = null;
    }
}