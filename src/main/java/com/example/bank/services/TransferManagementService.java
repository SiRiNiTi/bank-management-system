package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.models.TransactionHistory;

import java.math.BigDecimal;
import java.util.List;

public interface TransferManagementService {

    List<TransactionHistory> getAllTransaction(Long accountNum);

    Account putMoneyToAccount(long accountNum, BigDecimal amount);

    Account takeMoneyFromAccount(long accountNum, int pin, BigDecimal amount);

    void transferMoneyFromTo(long accountNumFrom, long accountNumTo, int pin, BigDecimal amount);
}
