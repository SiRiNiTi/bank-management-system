package com.example.bank.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "clients")
public class Client {

    @Id
    private Integer pin;
    private String name;

}
