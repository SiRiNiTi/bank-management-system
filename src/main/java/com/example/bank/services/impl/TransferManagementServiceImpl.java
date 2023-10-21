package com.example.bank.services.impl;

import com.example.bank.models.Account;
import com.example.bank.models.TransactionHistory;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransferMoneyReposiroty;
import com.example.bank.services.TransferManagementService;
import com.example.bank.core.exceptions.NotFoundException;
import com.example.bank.core.exceptions.TransferMoneyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TransferManagementServiceImpl implements TransferManagementService {

    private final TransferMoneyReposiroty transferMoneyReposiroty;
    private final AccountRepository accountRepository;

    public TransferManagementServiceImpl(TransferMoneyReposiroty transferMoneyReposiroty,
                                         AccountRepository accountRepository) {
        this.transferMoneyReposiroty = transferMoneyReposiroty;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<TransactionHistory> getAllTransaction(Long accountNum) {
        return accountNum == null ?
                (List<TransactionHistory>) transferMoneyReposiroty.findAll() :
                transferMoneyReposiroty.findAllByAccountAccountNum(accountNum);

    }

    @Transactional
    @Override
    public Account putMoneyToAccount(long accountNum, long amount) {
        Optional<Account> account = accountRepository.findById(accountNum);

        account.ifPresentOrElse(acc -> {
            long currentBalance = acc.getCurrentBalance();
            acc.setCurrentBalance(currentBalance + amount);

            var transaction = new TransactionHistory();
            transaction.setAccount(acc);
            transaction.setBalanceBefore(currentBalance);
            transaction.setBalanceAfter(currentBalance + amount);
            transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
            transferMoneyReposiroty.save(transaction);

        }, () -> {
            throw new NotFoundException(String.format("Account with number %s is not present", accountNum));
        });
        return account.get();
    }

    @Transactional
    @Override
    public Account takeMoneyFromAccount(long accountNum, int pin, long amount) {
        Optional<Account> account = accountRepository.findById(accountNum);

        account.ifPresentOrElse(acc -> {
            if (acc.getClient().getPin() != pin) {
                throw new TransferMoneyException(String.format("Wrong PIN for Account with number %s", accountNum));

            }
            long currentBalance = acc.getCurrentBalance();
            if (currentBalance - amount <= 0) {
                throw new TransferMoneyException(String.format("Money is not enough on Account with number %s", accountNum));
            }
            acc.setCurrentBalance(currentBalance - amount);

            var transaction = new TransactionHistory();
            transaction.setAccount(acc);
            transaction.setBalanceBefore(currentBalance);
            transaction.setBalanceAfter(currentBalance - amount);
            transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
            transferMoneyReposiroty.save(transaction);

        }, () -> {
            throw new NotFoundException(String.format("Account with number %s is not present", accountNum));
        });
        return account.get();
    }

    @Transactional
    @Override
    public void transferMoneyFromTo(long accountNumFrom, long accountNumTo, int pin, long amount) {
        Optional<Account> accountFrom = accountRepository.findById(accountNumFrom);
        Optional<Account> accountTo = accountRepository.findById(accountNumTo);

        accountFrom.ifPresentOrElse(sourceAccount -> {
            accountTo.ifPresentOrElse(targetAccount -> {
                    if (sourceAccount.getClient().getPin() != pin) {
                        throw new TransferMoneyException(String.format("Wrong PIN for Source Account with number %s", accountNumFrom));
                    }
                    long currentBalanceSourceAccount = sourceAccount.getCurrentBalance();
                    if (currentBalanceSourceAccount - amount < 0) {
                        throw new TransferMoneyException(String.format("Balance is not enough on Source Account with number %s", accountNumFrom));
                    }
                    var transactionTime = new Timestamp(System.currentTimeMillis());
                    sourceAccount.setCurrentBalance(currentBalanceSourceAccount - amount);

                    long currentBalanceTargetAccount = targetAccount.getCurrentBalance();
                    targetAccount.setCurrentBalance(currentBalanceTargetAccount + amount);

                    var transactionForSourceAccount = new TransactionHistory();
                    transactionForSourceAccount.setAccount(sourceAccount);
                    transactionForSourceAccount.setBalanceBefore(currentBalanceSourceAccount);
                    transactionForSourceAccount.setBalanceAfter(currentBalanceSourceAccount - amount);
                    transactionForSourceAccount.setTransactionTime(transactionTime);
                    var transactionForTargetAccount = new TransactionHistory();
                    transactionForTargetAccount.setAccount(targetAccount);
                    transactionForTargetAccount.setBalanceBefore(currentBalanceTargetAccount);
                    transactionForTargetAccount.setBalanceAfter(currentBalanceTargetAccount + amount);
                    transactionForTargetAccount.setTransactionTime(transactionTime);

                    transferMoneyReposiroty.save(transactionForSourceAccount);
                    transferMoneyReposiroty.save(transactionForTargetAccount);
                },
                () -> {
                    throw new NotFoundException(String.format("Target Account with number %s is not present", accountNumTo));
                });
        }, () -> {
            throw new NotFoundException(String.format("Source Account with number %s is not present", accountNumFrom));
        });
    }
}
