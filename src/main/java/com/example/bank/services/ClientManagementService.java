package com.example.bank.services;

import com.example.bank.models.Client;

import java.util.Optional;

public interface ClientManagementService {

    Optional<Client> findByPin(Integer pin);

    Client createClient(Integer pin, String name);
}
