package com.example.bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
@Data
@Table(name = "accounts")
public class Account {

    @Id
    @SequenceGenerator(name = "account_generator", sequenceName = "ACCOUNTS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    private long accountNum;

    private long currentBalance;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private Client client;

    @JsonIgnore
    @OneToMany(mappedBy = "account", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<TransactionHistory> transactions;

}
