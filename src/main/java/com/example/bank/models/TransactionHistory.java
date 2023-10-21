package com.example.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
//TODO use constant class for it
@Table(name = "transaction_history")
public class TransactionHistory {

    @Id
    @SequenceGenerator(name = "transaction_generator", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_generator")
    private long id;
    private long balanceBefore;
    private long balanceAfter;
    private Timestamp transactionTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_num", nullable = false)
    private Account account;

}
