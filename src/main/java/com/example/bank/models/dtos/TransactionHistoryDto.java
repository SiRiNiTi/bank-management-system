package com.example.bank.models.dtos;

import com.example.bank.models.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class TransactionHistoryDto {
    @Schema(description = "Transaction id", example = "1", requiredMode = REQUIRED)
    private long id;

    @Schema(description = "Balance before transaction", example = "100", requiredMode = REQUIRED)
    private long balanceBefore;

    @Schema(description = "Balance after transaction", example = "100", requiredMode = REQUIRED)
    private long balanceAfter;

    @Schema(description = "Transaction time", example = "2023-10-20T06:47:50.220", requiredMode = REQUIRED)
    private Timestamp transactionTime;

    @Schema(description = "Bank account number", example = "1", requiredMode = REQUIRED)
    private Account account;
}
