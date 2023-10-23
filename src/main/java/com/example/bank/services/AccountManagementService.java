package com.example.bank.services;

import com.example.bank.models.Account;

import java.util.List;

public interface AccountManagementService {

    Account createAccount(int pin, String name);

    List<Account> getAllAccounts();
}
