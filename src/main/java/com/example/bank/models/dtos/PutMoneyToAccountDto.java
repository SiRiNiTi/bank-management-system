package com.example.bank.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class PutMoneyToAccountDto {

    @Schema(description = "Bank account number", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "Account number is mandatory")
    private Long accountNum;

    @Schema(description = "Amount of money to put on the bank account", minimum = "0", example = "1", requiredMode = REQUIRED)
    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    private BigDecimal amount;
}
