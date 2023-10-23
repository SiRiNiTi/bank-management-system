package com.example.bank.controllers;

import com.example.bank.models.Account;
import com.example.bank.models.dtos.AccountDto;
import com.example.bank.models.dtos.ClientDto;
import com.example.bank.services.AccountManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountManagementService accountManagementService;
    private final ModelMapper modelMapper;

    public AccountController(AccountManagementService accountManagementService, ModelMapper modelMapper) {
        this.accountManagementService = accountManagementService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all Accounts")
    @GetMapping
    public List<AccountDto> getAllAccounts() {
        return modelMapper.map(
                accountManagementService.getAllAccounts(), new TypeToken<List<Account>>() {
                }.getType()
        );
    }

    @Operation(summary = "Create Account")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@Valid @RequestBody ClientDto client) throws URISyntaxException {
        Account account = accountManagementService.createAccount(client.getPin(), client.getName());
        return modelMapper.map(account, AccountDto.class);
    }
}
