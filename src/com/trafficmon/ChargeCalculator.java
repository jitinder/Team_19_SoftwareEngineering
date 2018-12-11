package com.trafficmon;

import java.math.BigDecimal;
import java.util.Calendar;

public class ChargeCalculator {

    /* Constants */
    private static final BigDecimal CHARGE_BEFORE_TWO_PM = new BigDecimal(6);
    private static final BigDecimal CHARGE_AFTER_TWO_PM = new BigDecimal(4);
    private static final int FOUR_HOURS_IN_MINS = 4 * 60;

    /**
     * Calculates the number of minutes between the startTime and endTime
     * @param startTimeMs is start time in milliseconds
     * @param endTimeMs is end time in milliseconds
     * @return number of minutes passed as an integer
     */
    public int getMinutesBetween(long startTimeMs, long endTimeMs) {
        return (int) Math.abs(Math.ceil((endTimeMs - startTimeMs) / (1000.0 * 60.0)));
    }

    /**
     * Checks whether the driver should be charged again by checking whether their last entry was four hours ago
     * @param firstEntryTimeMs is the first time of entry in milliseconds
     * @param currentTime is the current time (of re-entry) in milliseconds
     * @return true if user should be charged again
     * @return false if user should not be charged again
     */
    public boolean canChargeAgain(long firstEntryTimeMs, long currentTime) {
        int timeTaken = getMinutesBetween(firstEntryTimeMs, currentTime);
        if(timeTaken <= FOUR_HOURS_IN_MINS){
            return false;
        }
        return true;
    }

    /**
     * Calculates the {@link EntryEvent} charge based on the timestamp by comparing it with 2 pm of the same day.
     * @param timestamp is the timestamp the {@link EntryEvent} took place at
     * @return a BigDecimal value based on whether the {@link EntryEvent} occurred before or after 2 pm.
     */
    public BigDecimal getChargeByTime(long timestamp){
        Calendar calendarForTwoPm = Calendar.getInstance();
        Calendar calendarForTimestamp = Calendar.getInstance();
        calendarForTwoPm.set(Calendar.HOUR_OF_DAY, 14);
        calendarForTwoPm.set(Calendar.MINUTE, 0);
        calendarForTimestamp.setTimeInMillis(timestamp);

        if(calendarForTimestamp.after(calendarForTwoPm)) {
            return CHARGE_AFTER_TWO_PM;
        }
        return CHARGE_BEFORE_TWO_PM;
    }

}
