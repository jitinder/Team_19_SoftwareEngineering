package com.trafficmon;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


import static org.junit.Assert.*;

public class CongestionChargeSystemTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    /* Mock Account Service with the same basic functionality for testing custom accounts */
    private class MockAccountsService implements AccountsService {
        public List<Vehicle> accountVehicles = new ArrayList<>() {
            {
                this.add(Vehicle.withRegistration("A123 XYZ"));
                this.add(Vehicle.withRegistration("J091 4PY"));
                this.add(Vehicle.withRegistration("B246 XYZ"));
                this.add(Vehicle.withRegistration("C783 4TT"));
                this.add(Vehicle.withRegistration("D243 5PR"));
            }
        };

        public List<String> accountNames = new ArrayList<>(){
            {
                this.add("Fred Bloggs");
                this.add("John Smith");
                this.add("Trevor Yuen");
                this.add("Cayla Fleenor");
                this.add("Donald Avans");
            }
        };

        public List<BigDecimal> accountBalances = new ArrayList<>(){
            {
                this.add(new BigDecimal(100));
                this.add(new BigDecimal(10));
                this.add(new BigDecimal(0));
                this.add(new BigDecimal(50));
                this.add(new BigDecimal(20));
            }
        };

        public List<Account> accountsFull = new ArrayList<Account>(){
            {
                this.add(new Account(accountNames.get(0), accountVehicles.get(0), new BigDecimal(100)));
                this.add(new Account(accountNames.get(1), accountVehicles.get(1), new BigDecimal(10)));
                this.add(new Account(accountNames.get(2), accountVehicles.get(2), new BigDecimal(0)));
                this.add(new Account(accountNames.get(3), accountVehicles.get(3), new BigDecimal(50)));
                this.add(new Account(accountNames.get(4), accountVehicles.get(4), new BigDecimal(20)));
            }
        };

        public List<Account> accounts = new ArrayList<Account>();

        @Override
        public Account accountFor(Vehicle vehicle) throws AccountNotRegisteredException {
            Iterator i$ = this.accounts.iterator();

            Account account;
            do {
                if (!i$.hasNext()) {
                    throw new AccountNotRegisteredException(vehicle);
                }

                account = (Account)i$.next();
            } while(!account.getAssociatedVehicle().equals(vehicle));

            return account;
        }
    }

    private MockAccountsService mockAccountsService;
    private PenaltiesService mockPenaltiesService;
    private CongestionChargeSystem congestionChargeSystem;
    private List<Account> accountList;
    private List<Vehicle> vehicleList;
    private List<String> accountNamesList;
    private List<BigDecimal> accountBalanceList;
    private ByteArrayOutputStream outContent;

    @Before
    public void setup(){
        mockAccountsService = new MockAccountsService();
        accountList = mockAccountsService.accounts;
        vehicleList = mockAccountsService.accountVehicles;
        accountNamesList = mockAccountsService.accountNames;
        accountBalanceList = mockAccountsService.accountBalances;
        mockPenaltiesService = context.mock(PenaltiesService.class);
        congestionChargeSystem = new CongestionChargeSystem();
        congestionChargeSystem.setAccountsService(mockAccountsService);
        congestionChargeSystem.setPenaltiesService(mockPenaltiesService);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Copied from the {@link Account} class to return the same string representation of the {@link BigDecimal}
     *
     * @param amount is the {@link BigDecimal} to represent as String
     * @return the String representation of the {@link BigDecimal} along with a prepended £ symbol
     */
    private String format(BigDecimal amount) {
        BigDecimal rounded = amount.setScale(2, RoundingMode.HALF_UP);
        return "£" + rounded.toString();
    }

    //Transaction Tests - Correct Balance | Insufficient balance | Invalid Account

    // Possible errors in Mac because of the CRLF and LF encoding. This code is written to run on a Windows Machine
    // Remove the '\r' to run it on a Mac
    @Test
    public void executeTransactionForTestSufficientBalance(){
        BigDecimal charge = new BigDecimal(12);
        BigDecimal balance = new BigDecimal(100);
        BigDecimal expectedBalance = balance.subtract(charge);
        accountList.add(new Account(accountNamesList.get(0), vehicleList.get(0), balance));

        congestionChargeSystem.executeTransactionFor(vehicleList.get(0), charge);
        assertEquals("Charge made to account of "
                + accountList.get(0).getOwnerFullName() + ", " + format(charge) + " deducted, balance: " + format(expectedBalance) +"\r\n", outContent.toString());
    }

    @Test
    public void executeTransactionForTestInsufficientBalance(){
        BigDecimal charge = new BigDecimal(12);
        BigDecimal balance = new BigDecimal(10);
        accountList.add(new Account(accountNamesList.get(0), vehicleList.get(0), balance));

        context.checking(new Expectations(){
            {
                oneOf(CongestionChargeSystemTest.this.mockPenaltiesService).issuePenaltyNotice(vehicleList.get(0), charge);
            }
        });
        congestionChargeSystem.executeTransactionFor(vehicleList.get(0), charge);
    }

    @Test
    public void executeTransactionForTestInvalidAccount(){
        BigDecimal charge = new BigDecimal(12);
        BigDecimal balance = new BigDecimal(15);
        //Empty accountList

        context.checking(new Expectations(){
            {
                oneOf(CongestionChargeSystemTest.this.mockPenaltiesService).issuePenaltyNotice(vehicleList.get(0), charge);
            }
        });

        congestionChargeSystem.executeTransactionFor(vehicleList.get(0), charge);
    }

    @After
    public void reset(){
        mockAccountsService = null;
        mockPenaltiesService = null;
        congestionChargeSystem.setAccountsService(null);
        congestionChargeSystem.setPenaltiesService(null);
        congestionChargeSystem = null;
        System.setOut(System.out);
    }

}