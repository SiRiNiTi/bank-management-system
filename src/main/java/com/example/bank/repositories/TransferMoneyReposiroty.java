package com.example.bank.repositories;

import com.example.bank.models.TransactionHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferMoneyReposiroty extends CrudRepository<TransactionHistory, Integer> {

    List<TransactionHistory> findAllByAccountAccountNum(long accountNum);
}
