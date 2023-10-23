package com.example.bank.services.impl;

import com.example.bank.core.exceptions.NotFoundException;
import com.example.bank.core.exceptions.TransferMoneyException;
import com.example.bank.models.Account;
import com.example.bank.models.TransactionHistory;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransferMoneyReposiroty;
import com.example.bank.services.TransferManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        return transferMoneyReposiroty.findAllByAccountAccountNum(accountNum);

    }

    @Transactional
    @Override
    public Account putMoneyToAccount(long accountNum, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountNum);

        Account account = accountOpt.orElseThrow(() -> {
            throw new NotFoundException(String.format("Account with number %s is not present", accountNum));
        });
        var currentBalance = account.getCurrentBalance();
        account.setCurrentBalance(currentBalance.add(amount));

        var transaction = buildTransactionHistory(account, currentBalance, currentBalance.add(amount));
        transferMoneyReposiroty.save(transaction);

        return account;
    }

    @Transactional
    @Override
    public Account takeMoneyFromAccount(long accountNum, int pin, BigDecimal amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountNum);

        Account account = accountOpt.orElseThrow(() -> {
            throw new NotFoundException(String.format("Account with number %s is not present", accountNum));
        });

        if (account.getClient().getPin() != pin) {
            throw new TransferMoneyException(String.format("Wrong PIN for Account with number %s", accountNum));

        }
        var currentBalance = account.getCurrentBalance();
        if (currentBalance.subtract(amount).signum() < 0) {
            throw new TransferMoneyException(String.format("Money is not enough on Account with number %s", accountNum));
        }
        account.setCurrentBalance(currentBalance.subtract(amount));

        var transaction = buildTransactionHistory(account, currentBalance, currentBalance.subtract(amount));
        transferMoneyReposiroty.save(transaction);

        return account;
    }

    @Transactional
    @Override
    public void transferMoneyFromTo(long accountNumFrom, long accountNumTo, int pin, BigDecimal amount) {
        Optional<Account> accountFromOpt = accountRepository.findById(accountNumFrom);
        Optional<Account> accountToOpt = accountRepository.findById(accountNumTo);

        Account sourceAccount = accountFromOpt.orElseThrow(() -> {
            throw new NotFoundException(String.format("Source Account with number %s is not present", accountNumFrom));
        });

        Account targetAccount = accountToOpt.orElseThrow(() -> {
            throw new NotFoundException(String.format("Target Account with number %s is not present", accountNumTo));
        });

        if (sourceAccount.getClient().getPin() != pin) {
            throw new TransferMoneyException(String.format("Wrong PIN for Source Account with number %s", accountNumFrom));
        }
        var currentBalanceSourceAccount = sourceAccount.getCurrentBalance();
        if (currentBalanceSourceAccount.subtract(amount).signum() < 0) {
            throw new TransferMoneyException(String.format("Balance is not enough on Source Account with number %s", accountNumFrom));
        }
        sourceAccount.setCurrentBalance(currentBalanceSourceAccount.subtract(amount));

        var currentBalanceTargetAccount = targetAccount.getCurrentBalance();
        targetAccount.setCurrentBalance(currentBalanceTargetAccount.add(amount));

        var transactionForSourceAccount = buildTransactionHistory(sourceAccount,
                currentBalanceSourceAccount,
                currentBalanceSourceAccount.subtract(amount));

        var transactionForTargetAccount = buildTransactionHistory(targetAccount,
                currentBalanceTargetAccount,
                currentBalanceTargetAccount.add(amount));

        transferMoneyReposiroty.save(transactionForSourceAccount);
        transferMoneyReposiroty.save(transactionForTargetAccount);

    }

    private TransactionHistory buildTransactionHistory(Account account,
                                                       BigDecimal balanceBefore,
                                                       BigDecimal balanceAfter) {
        var transaction = new TransactionHistory()
                .setAccount(account)
                .setBalanceBefore(balanceBefore)
                .setBalanceAfter(balanceAfter)
                .setTransactionTime(new Timestamp(System.currentTimeMillis()));
        return transaction;
    }
}
