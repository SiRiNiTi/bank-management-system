package com.example.bank.services;

import com.example.bank.core.exceptions.NotFoundException;
import com.example.bank.core.exceptions.TransferMoneyException;
import com.example.bank.models.Account;
import com.example.bank.models.Client;
import com.example.bank.models.TransactionHistory;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransferMoneyReposiroty;
import com.example.bank.services.impl.TransferManagementServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TransferManagementServiceTest {

    @InjectMocks
    private TransferManagementServiceImpl transferManagementService;
    @Mock
    private TransferMoneyReposiroty transferMoneyReposiroty;
    @Mock
    private AccountRepository accountRepository;
    @Captor
    ArgumentCaptor<TransactionHistory> transactionHistoryCaptor;

    private Client client1 = new Client();
    private Client client2 = new Client();

    @BeforeEach
    public void init() {
        client1.setName("Ivanov Ivan");
        client1.setPin(1111);
        client2.setName("Fedorov Fedor");
        client2.setPin(2222);
    }

    @Test
    void getAllTransaction_givenValidInput_thenReturnAllTransactionsForAccount() {
        var account = new Account();

        var tr = new TransactionHistory();
        tr.setId(1L);
        tr.setBalanceBefore(new BigDecimal(1000));
        tr.setBalanceAfter(new BigDecimal(100));
        tr.setAccount(account);

        var tr2 = new TransactionHistory();
        tr2.setId(2L);
        tr2.setBalanceBefore(new BigDecimal(1000));
        tr2.setBalanceAfter(new BigDecimal(100));
        tr2.setAccount(account);

        Mockito.doReturn(List.of(tr, tr2)).when(transferMoneyReposiroty).findAllByAccountAccountNum(anyLong());
        List<TransactionHistory> allTransactions = transferManagementService.getAllTransaction(1L);
        Assertions.assertFalse(allTransactions.isEmpty());
        Assertions.assertArrayEquals(List.of(tr, tr2).toArray(), allTransactions.toArray());
    }

    @Test
    void getAllTransaction_givenValidInput_thenReturnAllTransactions() {
        var account1 = new Account();
        var account2 = new Account();

        var tr = new TransactionHistory();
        tr.setId(1L);
        tr.setBalanceBefore(new BigDecimal(1000));
        tr.setBalanceAfter(new BigDecimal(100));
        tr.setAccount(account1);

        var tr2 = new TransactionHistory();
        tr2.setId(2L);
        tr2.setBalanceBefore(new BigDecimal(1000));
        tr2.setBalanceAfter(new BigDecimal(100));
        tr2.setAccount(account2);

        Mockito.doReturn(List.of(tr, tr2)).when(transferMoneyReposiroty).findAll();
        List<TransactionHistory> allTransactions = transferManagementService.getAllTransaction(null);
        Assertions.assertFalse(allTransactions.isEmpty());
        Assertions.assertArrayEquals(List.of(tr, tr2).toArray(), allTransactions.toArray());
    }

    @Test
    void getAllTransaction_givenValidInputAndTransactionsAreAbsent_thenReturEmptyList() {
        Mockito.doReturn(List.of()).when(transferMoneyReposiroty).findAll();
        List<TransactionHistory> allTransactions = transferManagementService.getAllTransaction(null);
        Assertions.assertTrue(allTransactions.isEmpty());
    }

    @Test
    void putMoneyToAccount_givenValidInputAndAccountFound_thenNewTransactionToPutMoneyCreated() {
        var account = new Account();
        account.setClient(client1);
        account.setAccountNum(555L);
        account.setCurrentBalance(new BigDecimal(0));

        var tr = new TransactionHistory();
        tr.setBalanceBefore(new BigDecimal(0));
        tr.setBalanceAfter(new BigDecimal(100));
        tr.setAccount(account);

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(any());
        Mockito.doReturn(new TransactionHistory()).when(transferMoneyReposiroty).save(any());

        Account actualAccount = transferManagementService.putMoneyToAccount(1, new BigDecimal(100));
        Mockito.verify(transferMoneyReposiroty).save(transactionHistoryCaptor.capture());
        TransactionHistory actualTr = transactionHistoryCaptor.getValue();

        Assertions.assertEquals(100, actualAccount.getCurrentBalance());
        Assertions.assertEquals(tr.getBalanceBefore(), actualTr.getBalanceBefore());
        Assertions.assertEquals(tr.getBalanceAfter(), actualTr.getBalanceAfter());
        Assertions.assertEquals(tr.getAccount().getCurrentBalance(), actualTr.getAccount().getCurrentBalance());
    }

    @Test
    void putMoneyToAccount_givenValidInputAndAccountNotFound_thenThrowException() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(any());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> transferManagementService.putMoneyToAccount(1, new BigDecimal(100)));
        Assertions.assertEquals("Account with number 1 is not present", exception.getMessage());
    }

    @Test
    void takeMoneyFromAccount_givenValidInputAndAccountFound_thenNewTransactionToTakeMoneyCreated() {
        var account = new Account();
        account.setClient(client1);
        account.setAccountNum(555L);
        account.setCurrentBalance(new BigDecimal(1000));

        var tr = new TransactionHistory();
        tr.setBalanceBefore(new BigDecimal(1000));
        tr.setBalanceAfter(new BigDecimal(100));
        tr.setAccount(account);

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(any());
        Mockito.doReturn(tr).when(transferMoneyReposiroty).save(any());

        Account actualAccount = transferManagementService.takeMoneyFromAccount(1, 1111, new BigDecimal(900));
        Mockito.verify(transferMoneyReposiroty).save(transactionHistoryCaptor.capture());
        TransactionHistory actualTr = transactionHistoryCaptor.getValue();

        Assertions.assertEquals(100, actualAccount.getCurrentBalance());
        Assertions.assertEquals(tr.getBalanceAfter(), actualTr.getBalanceAfter());
        Assertions.assertEquals(tr.getBalanceBefore(), actualTr.getBalanceBefore());
        Assertions.assertEquals(tr.getAccount().getCurrentBalance(), actualTr.getAccount().getCurrentBalance());
    }

    @Test
    void takeMoneyToAccount_givenValidInputAndAccountNotFound_thenThrowException() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(any());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> transferManagementService.takeMoneyFromAccount(1, 1111, new BigDecimal(500)));
        Assertions.assertEquals("Account with number 1 is not present", exception.getMessage());
    }

    @Test
    void takeMoneyToAccount_givenValidInputAndAccountFoundAndInvalidPin_thenThrowExceptionWrongPin() {
        var account = new Account();
        account.setClient(client1);
        account.setAccountNum(555L);
        account.setCurrentBalance(new BigDecimal(1000));

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(any());
        TransferMoneyException exception = Assertions.assertThrows(TransferMoneyException.class,
                () -> transferManagementService.takeMoneyFromAccount(1, 5555, new BigDecimal(500)));
        Assertions.assertEquals("Wrong PIN for Account with number 1", exception.getMessage());
    }

    @Test
    void takeMoneyToAccount_givenValidInputAndAccountFound_thenThrowExceptionBalanceNotEnough() {
        var account = new Account();
        account.setClient(client1);
        account.setAccountNum(555L);
        account.setCurrentBalance(new BigDecimal(100));

        Mockito.doReturn(Optional.of(account)).when(accountRepository).findById(any());
        TransferMoneyException exception = Assertions.assertThrows(TransferMoneyException.class,
                () -> transferManagementService.takeMoneyFromAccount(1, 1111, new BigDecimal(500)));
        Assertions.assertEquals("Money is not enough on Account with number 1", exception.getMessage());
    }

    @Test
    void transferMoneyFromTo_givenValidInput_then2TransactionCreated() {
        var sourceAccount = new Account();
        sourceAccount.setClient(client1);
        sourceAccount.setAccountNum(555L);
        sourceAccount.setCurrentBalance(new BigDecimal(1000));

        var targetAccount = new Account();
        targetAccount.setClient(client2);
        targetAccount.setAccountNum(666L);
        targetAccount.setCurrentBalance(new BigDecimal(200));

        var trOnSourceAccount = new TransactionHistory();
        trOnSourceAccount.setBalanceBefore(new BigDecimal(1000));
        trOnSourceAccount.setBalanceAfter(new BigDecimal(500));
        trOnSourceAccount.setAccount(sourceAccount);
        var trOnTargetAccount = new TransactionHistory();
        trOnTargetAccount.setBalanceBefore(new BigDecimal(200));
        trOnTargetAccount.setBalanceAfter(new BigDecimal(700));
        trOnTargetAccount.setAccount(targetAccount);

        Mockito.doReturn(Optional.of(sourceAccount))
                .doReturn(Optional.of(targetAccount))
                .when(accountRepository).findById(any());
        Mockito.doReturn(trOnSourceAccount).when(transferMoneyReposiroty).save(any());

        transferManagementService.transferMoneyFromTo(555, 666, 1111, new BigDecimal(500));
        Mockito.verify(transferMoneyReposiroty, times(2)).save(transactionHistoryCaptor.capture());
        List<TransactionHistory> transactions = transactionHistoryCaptor.getAllValues();

        transactions.stream()
                .filter(tr -> tr.getAccount().getAccountNum() == sourceAccount.getAccountNum())
                .findFirst()
                .ifPresentOrElse(actualTr -> {
                    Assertions.assertEquals(trOnSourceAccount.getBalanceBefore(), actualTr.getBalanceBefore());
                    Assertions.assertEquals(trOnSourceAccount.getBalanceAfter(), actualTr.getBalanceAfter());
                }, () -> Assertions.fail("Transaction for Source Account is not present"));

        transactions.stream()
                .filter(tr -> tr.getAccount().getAccountNum() == targetAccount.getAccountNum())
                .findFirst()
                .ifPresentOrElse(actualTr -> {
                    Assertions.assertEquals(trOnTargetAccount.getBalanceBefore(), actualTr.getBalanceBefore());
                    Assertions.assertEquals(trOnTargetAccount.getBalanceAfter(), actualTr.getBalanceAfter());
                }, () -> Assertions.fail("Transaction for Target Account is not present"));
    }

    @Test
    void transferMoneyFromTo_givenValidInputAndSourceAccountNotFound_thenThrowException() {
        Mockito.doReturn(Optional.empty()).when(accountRepository).findById(any());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> transferManagementService.transferMoneyFromTo(555, 666, 1111, new BigDecimal(500)));
        Assertions.assertEquals("Source Account with number 555 is not present", exception.getMessage());
    }

    @Test
    void transferMoneyFromTo_givenValidInputAndTargetAccountNotFound_thenThrowException() {
        Mockito.doReturn(Optional.of(new Account()))
                .doReturn(Optional.empty())
                .when(accountRepository).findById(any());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> transferManagementService.transferMoneyFromTo(555, 666, 1111, new BigDecimal(500)));
        Assertions.assertEquals("Target Account with number 666 is not present", exception.getMessage());
    }

    @Test
    void transferMoneyFromTo_givenInValidPin_thenThrowExceptionWrongPin() {
        var sourceAccount = new Account();
        sourceAccount.setClient(client1);
        sourceAccount.setAccountNum(555L);
        sourceAccount.setCurrentBalance(new BigDecimal(1000));

        var targetAccount = new Account();
        targetAccount.setClient(client2);
        targetAccount.setAccountNum(666L);
        targetAccount.setCurrentBalance(new BigDecimal(200));

        var trOnSourceAccount = new TransactionHistory();
        trOnSourceAccount.setBalanceBefore(new BigDecimal(1000));
        trOnSourceAccount.setBalanceAfter(new BigDecimal(500));
        trOnSourceAccount.setAccount(sourceAccount);
        var trOnTargetAccount = new TransactionHistory();
        trOnTargetAccount.setBalanceBefore(new BigDecimal(200));
        trOnTargetAccount.setBalanceAfter(new BigDecimal(700));
        trOnTargetAccount.setAccount(targetAccount);

        Mockito.doReturn(Optional.of(sourceAccount))
                .doReturn(Optional.of(targetAccount))
                .when(accountRepository).findById(any());

        TransferMoneyException exception = Assertions.assertThrows(TransferMoneyException.class,
                () -> transferManagementService.transferMoneyFromTo(555, 666, 5463, new BigDecimal(500)));
        Assertions.assertEquals("Wrong PIN for Source Account with number 555", exception.getMessage());
    }

    @Test
    void transferMoneyFromTo_givenValidInputAndAccountFound_thenThrowExceptionBalanceNotEnough() {
        var sourceAccount = new Account();
        sourceAccount.setClient(client1);
        sourceAccount.setAccountNum(555L);
        sourceAccount.setCurrentBalance(new BigDecimal(100));

        var targetAccount = new Account();
        targetAccount.setClient(client2);
        targetAccount.setAccountNum(666L);
        targetAccount.setCurrentBalance(new BigDecimal(200));

        var trOnSourceAccount = new TransactionHistory();
        trOnSourceAccount.setBalanceBefore(new BigDecimal(1000));
        trOnSourceAccount.setBalanceAfter(new BigDecimal(500));
        trOnSourceAccount.setAccount(sourceAccount);
        var trOnTargetAccount = new TransactionHistory();
        trOnTargetAccount.setBalanceBefore(new BigDecimal(200));
        trOnTargetAccount.setBalanceAfter(new BigDecimal(700));
        trOnTargetAccount.setAccount(targetAccount);

        Mockito.doReturn(Optional.of(sourceAccount))
                .doReturn(Optional.of(targetAccount))
                .when(accountRepository).findById(any());

        TransferMoneyException exception = Assertions.assertThrows(TransferMoneyException.class,
                () -> transferManagementService.transferMoneyFromTo(555, 666, 1111, new BigDecimal(500)));
        Assertions.assertEquals("Balance is not enough on Source Account with number 555", exception.getMessage());
    }

    //todo Add test for transfer operation for One Client
}