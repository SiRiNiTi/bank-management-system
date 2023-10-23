package com.example.bank.services.impl;

import com.example.bank.models.Account;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.services.AccountManagementService;
import com.example.bank.services.ClientManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountManagementServiceImpl implements AccountManagementService {

    private final ClientManagementService clientManagementService;
    private final AccountRepository accountRepository;

    public AccountManagementServiceImpl(ClientManagementService clientManagementService, AccountRepository accountRepository) {
        this.clientManagementService = clientManagementService;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public Account createAccount(int pin, String name) {
        var account = new Account()
                .setCurrentBalance(new BigDecimal(0));
        var existingClient = clientManagementService.findByPin(pin);
        existingClient.ifPresentOrElse(client -> {
                    account.setClient(client);
                    accountRepository.save(account);
                },
                () -> {
                    var client = clientManagementService.createClient(pin, name);
                    account.setClient(client);
                    accountRepository.save(account);
                });
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

}
