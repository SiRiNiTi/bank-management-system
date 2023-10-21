package com.example.bank.services.impl;

import com.example.bank.models.Account;
import com.example.bank.models.Client;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.ClientRepository;
import com.example.bank.services.AccountManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountManagementServiceImpl implements AccountManagementService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    public AccountManagementServiceImpl(ClientRepository clientRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public Account createAccount(int pin, String name) {
        var account = new Account();
        var existingClient = clientRepository.findById(pin);
        existingClient.ifPresentOrElse(client -> {
                    account.setClient(existingClient.get());
                    accountRepository.save(account);
                },
                () -> {
                    var client = new Client();
                    client.setPin(pin);
                    client.setName(name);
                    var savedClient = clientRepository.save(client);
                    account.setClient(savedClient);
                    accountRepository.save(account);
                });
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

}
