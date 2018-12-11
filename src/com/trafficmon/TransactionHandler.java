package com.trafficmon;

import java.math.BigDecimal;

public interface TransactionHandler {
    void executeTransactionFor(Object object, BigDecimal charge);
    void executeAllTransactions();
}
