package com.example.bank.controllers;

import com.example.bank.models.Account;
import com.example.bank.models.dtos.AccountDto;
import com.example.bank.models.dtos.ClientDto;
import com.example.bank.services.AccountManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountManagementService accountManagementService;
    private final ModelMapper modelMapper;

    public AccountController(AccountManagementService accountManagementService, ModelMapper modelMapper) {
        this.accountManagementService = accountManagementService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all Accounts")
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountManagementService.getAllAccounts();
    }

    @Operation(summary = "Create Account")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody ClientDto client) throws URISyntaxException {
        Account account = accountManagementService.createAccount(client.getPin(), client.getName());
        return ResponseEntity.created(new URI("/account/" + account.getAccountNum()))
                .body(modelMapper.map(account, AccountDto.class));
    }
}
