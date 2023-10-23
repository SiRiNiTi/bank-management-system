package com.example.bank.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    @Schema(description = "Bank account number", example = "1")
    private Long accountNum;

    @Schema(description = "Current balance of bank account")
    private BigDecimal currentBalance;

    @Schema(description = "Holder of bank account")
    private ClientDto client;
}
