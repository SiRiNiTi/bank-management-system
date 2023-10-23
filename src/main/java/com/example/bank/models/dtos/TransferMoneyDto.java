package com.example.bank.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class TransferMoneyDto {

    @Schema(description = "Source bank account number", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "Source Account number is mandatory")
    private Long accountFrom;

    @Schema(description = "Target bank account number", example = "1", requiredMode = REQUIRED)
    @NotNull(message = "Target Account number is mandatory")
    private Long accountTo;

    @Schema(description = "PIN code of source bank account (4 number)", example = "1111", requiredMode = REQUIRED)
    @NotNull(message = "PIN of Source Account is mandatory")
    @Min(1000)
    @Max(9999)
    private Integer pin;

    @Schema(description = "Amount of money to take from the bank account", minimum = "0", example = "1", requiredMode = REQUIRED)
    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    private BigDecimal amount;

    @AssertTrue(message = "Source Account should not be equal Target Account")
    private boolean isValid() {
        return accountFrom != accountTo;
    }
}
