package com.example.bank.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "transaction_history")
public class TransactionHistory {

    @Id
    @SequenceGenerator(name = "transaction_generator", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_generator")
    private Long id;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private Timestamp transactionTime;

    @ManyToOne
    @JoinColumn(name = "account_num", nullable = false)
    private Account account;

}
