package com.example.bank.controllers;

import com.example.bank.models.dtos.*;
import com.example.bank.services.TransferManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransferManagementService transferManagementService;
    private final ModelMapper modelMapper;

    public TransactionController(TransferManagementService transferManagementService, ModelMapper modelMapper) {
        this.transferManagementService = transferManagementService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Put money to the account")
    @PostMapping("/refill")
    public ResponseEntity<AccountDto> putMoneyToAccount(@Valid @RequestBody PutMoneyToAccountDto putMoney) {
        var account = transferManagementService.putMoneyToAccount(putMoney.getAccountNum(), putMoney.getAmount());
        return ResponseEntity.ok(modelMapper.map(account, AccountDto.class));
    }

    @Operation(summary = "Take money from the account")
    @PostMapping("/withdraw")
    public ResponseEntity<AccountDto> takeMoneyFromAccount(@Valid @RequestBody TakeMoneyFromAccountDto withdrawMoney) {
        var account = transferManagementService.takeMoneyFromAccount(withdrawMoney.getAccountNum(),
                withdrawMoney.getPin(),
                withdrawMoney.getAmount());
        return ResponseEntity.ok(modelMapper.map(account, AccountDto.class));
    }

    @Operation(summary = "Transfer money between accounts")
    @PostMapping("/transfer")
    public ResponseEntity<Void> transferMoneyFromTo(@Valid @RequestBody TransferMoneyDto transferMoney) {
        transferManagementService.transferMoneyFromTo(transferMoney.getAccountFrom(),
                transferMoney.getAccountTo(),
                transferMoney.getPin(),
                transferMoney.getAmount());
        return ResponseEntity.ok().build();
    }
}
