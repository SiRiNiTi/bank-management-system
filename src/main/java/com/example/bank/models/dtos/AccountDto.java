package com.example.bank.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AccountDto {
    @Schema(description = "Bank account number", example = "1")
    private long accountNum;

    @Schema(description = "Current balance of bank account")
    private long currentBalance;

    @Schema(description = "Holder of bank account")
    private ClientDto client;
}
