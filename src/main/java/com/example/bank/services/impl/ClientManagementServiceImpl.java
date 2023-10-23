package com.example.bank.services.impl;

import com.example.bank.models.Client;
import com.example.bank.repositories.ClientRepository;
import com.example.bank.services.ClientManagementService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientManagementServiceImpl implements ClientManagementService {

    private final ClientRepository clientRepository;

    public ClientManagementServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public Optional<Client> findByPin(Integer pin) {
        return clientRepository.findById(pin);
    }

    @Override
    public Client createClient(Integer pin, String name) {
        var client = new Client();
        client.setPin(pin);
        client.setName(name);
        return clientRepository.save(client);
    }
}
