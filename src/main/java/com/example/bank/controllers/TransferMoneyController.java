package com.example.bank.controllers;

import com.example.bank.models.dtos.*;
import com.example.bank.services.TransferManagementService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/money")
public class TransferMoneyController {

    private final TransferManagementService transferManagementService;
    private final ModelMapper modelMapper;

    public TransferMoneyController(TransferManagementService transferManagementService, ModelMapper modelMapper) {
        this.transferManagementService = transferManagementService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get")
    public List<TransactionHistoryDto> getAllTransactions(
            @Parameter(name = "accountNum", description = "Bank Account number", example = "1")
//            @NotNull(message = "AccountNum is mandatory")
            @RequestParam(required = false) Long accountNum) {
        return modelMapper.map(
                transferManagementService.getAllTransaction(accountNum),
                new TypeToken<List<TransactionHistoryDto>>() {}.getType()
        );
    }


    @PostMapping("/put")
    public ResponseEntity<AccountDto> putMoneyToAccount(@Valid @RequestBody PutMoneyToAccountDto putMoney) {
        var account = transferManagementService.putMoneyToAccount(putMoney.getAccountNum(), putMoney.getAmount());
        return ResponseEntity.ok(modelMapper.map(account, AccountDto.class));
    }

    @PostMapping("/take")
    public ResponseEntity<AccountDto> takeMoneyFromAccount(@Valid @RequestBody TakeMoneyFromAccountDto withdrawMoney) {
        var account = transferManagementService.takeMoneyFromAccount(withdrawMoney.getAccountNum(),
                withdrawMoney.getPin(),
                withdrawMoney.getAmount());
        return ResponseEntity.ok(modelMapper.map(account, AccountDto.class));
    }

    @PostMapping("/transfer")
    public ResponseEntity transferMoneyFromTo(@Valid @RequestBody TransferMoneyDto transferMoney) {
        transferManagementService.transferMoneyFromTo(transferMoney.getAccountFrom(),
                transferMoney.getAccountTo(),
                transferMoney.getPin(),
                transferMoney.getAmount());
        return ResponseEntity.ok().build();
    }
}
