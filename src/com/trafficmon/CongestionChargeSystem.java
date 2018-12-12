package com.trafficmon;

import java.math.BigDecimal;
import java.util.*;

public class CongestionChargeSystem implements TransactionHandler{

    /* Constants */
    private AccountsService accountsService = RegisteredCustomerAccountsService.getInstance();
    private PenaltiesService penaltiesService = OperationsTeam.getInstance();
    private CongestionChargeCalculator chargeCalculator = new CongestionChargeCalculator();

    /* Singleton Class code */
    private static CongestionChargeSystem instance = new CongestionChargeSystem();

    /* Getters and Setters */
    /**
     * Instantiates the {@link CongestionChargeSystem} at runtime
     *
     * @return the instance of the class
     */
    public static CongestionChargeSystem getInstance() {
        return instance;
    }

    /**
     * Used to set a custom {@link AccountsService} for this class
     *
     * @param newAccountsService is a valid {@link AccountsService} instance
     */
    public void setAccountsService(AccountsService newAccountsService){
        accountsService = newAccountsService;
    }

    /**
     * Used to set a custom {@link PenaltiesService} for this class
     * @param newPenaltiesService is a valid {@link PenaltiesService} instance
     */
    public void setPenaltiesService(PenaltiesService newPenaltiesService){
        penaltiesService = newPenaltiesService;
    }

    /* Transaction Methods */
    /**
     * * Charges the Vehicle owner the specified amount of money
     *
     * @param vehicle is the {@link Vehicle} associated with an {@link Account}
     * @param charge is the {@link BigDecimal} value to charge the {@link Account}
     * @param accountsService refers to the appropriate service for handling accounts - {@link RegisteredCustomerAccountsService} in this case.
     * @throws AccountNotRegisteredException if the {@link Vehicle} doesn't belong to a valid {@link Account} in {@link RegisteredCustomerAccountsService}
     * @throws InsufficientCreditException if the registered {@link Account} doesn't have enough balance in {@link RegisteredCustomerAccountsService}
     */
    private void chargeVehicle(Vehicle vehicle, BigDecimal charge, AccountsService accountsService) throws AccountNotRegisteredException, InsufficientCreditException {
        accountsService.accountFor(vehicle).deduct(charge);
    }

    /**
     * Executes a particular transaction and handles any errors thrown by the process. Overrides the method from {@link TransactionHandler}.
     * Uses the {@link AccountsService} and {@link PenaltiesService} designated to the class
     *
     * @param object refers to the {@link Vehicle} in this case.
     * @param charge refers to the {@link BigDecimal} charge.
     */
    @Override
    public void executeTransactionFor(Object object, BigDecimal charge) {
        Vehicle vehicle = (Vehicle) object;
        try {
            chargeVehicle(vehicle, charge, accountsService);
        } catch (AccountNotRegisteredException e) {
            penaltiesService.issuePenaltyNotice(vehicle, charge);
        } catch (InsufficientCreditException e) {
            penaltiesService.issuePenaltyNotice(vehicle, charge);
        }
    }

    /**
     * Executes all transactions for the day's activity using the {@link ChargeCalculator} which returns a list of all vehicles and their respective charges from {@link EventLog}.
     * Overrides the method from {@link TransactionHandler}.
     */
    @Override
    public void executeAllTransactions() {
        Map<Vehicle, BigDecimal> chargeList = chargeCalculator.getChargeList();
        for(Vehicle vehicle : chargeList.keySet()){
            executeTransactionFor(vehicle, chargeList.get(vehicle));
        }
    }

    /**
     *
     * For Testing
     *
     * **/
    public static void main(String args[]){
        CongestionChargeSystem congestionChargeSystem = new CongestionChargeSystem();

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(Vehicle.withRegistration("A123 XYZ"));
        vehicles.add(Vehicle.withRegistration("J091 4PY"));
        vehicles.add(Vehicle.withRegistration("B246 XYZ"));
        vehicles.add(Vehicle.withRegistration("C783 4TT"));
        vehicles.add(Vehicle.withRegistration("D243 5PR"));
/*
        long sysTime = System.currentTimeMillis();
        congestionChargeSystem.eventLog.add(new EntryEvent(vehicles.get(0), sysTime));
        congestionChargeSystem.eventLog.add(new ExitEvent(vehicles.get(0), sysTime + ONE_HOUR_IN_MS));
        congestionChargeSystem.eventLog.add(new EntryEvent(vehicles.get(0), sysTime + (5*ONE_HOUR_IN_MS)));
        congestionChargeSystem.eventLog.add(new ExitEvent(vehicles.get(0), sysTime + (7*ONE_HOUR_IN_MS)));

        Map<Vehicle, BigDecimal> chargePerVehicle = congestionChargeSystem.getChargeList();
        for(Vehicle v: chargePerVehicle.keySet()) {
            System.out.println(v + " : " + chargePerVehicle.get(v));
        }
        */
    }
}
