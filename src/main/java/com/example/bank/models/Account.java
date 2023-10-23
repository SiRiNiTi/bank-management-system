package com.example.bank.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "accounts")
public class Account {

    @Id
    @SequenceGenerator(name = "account_generator", sequenceName = "ACCOUNTS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    private Long accountNum;

    private BigDecimal currentBalance;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private Client client;

    @OneToMany(mappedBy = "account", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<TransactionHistory> transactions;

}
