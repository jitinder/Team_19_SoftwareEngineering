package com.trafficmon;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CongestionChargeCalculator extends ChargeCalculator {

    /* Global Variables */
    private EventLog eventLog = EventLog.getInstance();


    /* Methods to Return Charges for Vehicle Activity */
    /**
     * Calculates the overall charge to all vehicles in the eventLog over the whole day considering all their {@link ZoneBoundaryCrossing}
     * activities logged in the list.
     *
     * @return a {@link HashMap} with the Vehicle as a key and {@link BigDecimal} containing the total money to charge the vehicle
     * based on the day's activity
     */
    public Map<Vehicle, BigDecimal> getChargeList(){
        Map<Vehicle, List<ZoneBoundaryCrossing>> crossingsPerVehicle = eventLog.getCrossingsPerVehicle();
        Map<Vehicle, BigDecimal> chargePerVehicle = new HashMap<>();

        for(Vehicle vehicle: crossingsPerVehicle.keySet()){

            BigDecimal vehicleCharge = new BigDecimal(0);
            List<ZoneBoundaryCrossing> crossingsByVehicle = crossingsPerVehicle.get(vehicle);

            if (!eventLog.checkOrderingOf(crossingsByVehicle)) {
                OperationsTeam.getInstance().triggerInvestigationInto(vehicle);
            } else {
                EntryEvent lastChargedEntry = (EntryEvent) crossingsByVehicle.get(0);
                vehicleCharge = vehicleCharge.add(getChargeByTime(lastChargedEntry.timestamp()));

                for(ZoneBoundaryCrossing crossing : crossingsByVehicle.subList(1, crossingsByVehicle.size())){
                    if(crossing instanceof ExitEvent){
                        int durationInArea = getMinutesBetween(lastChargedEntry.timestamp(), crossing.timestamp());
                        if(durationInArea > FOUR_HOURS_IN_MINS){
                            vehicleCharge = CHARGE_OVERTIME;
                            break;
                        }
                    }

                    if(crossing instanceof EntryEvent){
                        if(canChargeAgain(lastChargedEntry.timestamp(), crossing.timestamp())) {
                            vehicleCharge = vehicleCharge.add(getChargeByTime(crossing.timestamp()));
                            lastChargedEntry = (EntryEvent) crossing;
                        }
                    }
                }
            }
            chargePerVehicle.put(vehicle, vehicleCharge);
        }

        return chargePerVehicle;
    }

    /**
     * Gives the overall charge for a specified vehicle based on its activity in the eventLog.
     *
     * @param vehicle is the Vehicle whose charge is being determined
     * @return a {@link BigDecimal} object with the charge or 0 value if the vehicle isn't in the eventLog
     */
    public BigDecimal getChargeForVehicle(Vehicle vehicle){
        Map<Vehicle, BigDecimal> chargeList = getChargeList();
        if(chargeList.keySet().contains(vehicle)) {
            return chargeList.get(vehicle);
        }
        return new BigDecimal(0);
    }

}
