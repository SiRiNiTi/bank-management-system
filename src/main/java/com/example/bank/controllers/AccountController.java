package com.example.bank.controllers;

import com.example.bank.models.Account;
import com.example.bank.models.dtos.AccountDto;
import com.example.bank.models.dtos.ClientDto;
import com.example.bank.models.dtos.TransactionHistoryDto;
import com.example.bank.services.AccountManagementService;
import com.example.bank.services.TransferManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    private final TransferManagementService transferManagementService;
    private final ModelMapper modelMapper;

    public AccountController(AccountManagementService accountManagementService,
                             TransferManagementService transferManagementService,
                             ModelMapper modelMapper) {
        this.accountManagementService = accountManagementService;
        this.transferManagementService = transferManagementService;
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

    @Operation(summary = "Get all transactions by account")
    @GetMapping("/{accountNum}/transactions")
    public List<TransactionHistoryDto> getAllTransactions(
            @Parameter(name = "accountNum", description = "Bank Account number", example = "1")
            @PathVariable Long accountNum) {
        return modelMapper.map(
                transferManagementService.getAllTransaction(accountNum),
                new TypeToken<List<TransactionHistoryDto>>() {
                }.getType()
        );
    }
}
