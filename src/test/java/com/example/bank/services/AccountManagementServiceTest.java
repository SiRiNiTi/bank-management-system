package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.models.Client;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.ClientRepository;
import com.example.bank.services.impl.AccountManagementServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;


@ExtendWith(MockitoExtension.class)
public class AccountManagementServiceTest {

    @InjectMocks
    private AccountManagementServiceImpl accountManagementService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;

    private Client client1 = new Client();
    private Account account1 = new Account();
    private Account account2 = new Account();

    @BeforeEach
    public void init() {
        client1.setName("Ivanov Ivan");
        client1.setPin(1234);

        account1.setClient(client1);
        account1.setAccountNum(958);
        account2.setClient(client1);
        account2.setAccountNum(996);
    }

    @Test
    public void getAllAccounts() {
        List<Account> expectedAccounts = List.of(this.account1, account2);
        Mockito.doReturn(expectedAccounts).when(accountRepository).findAll();

        List<Account> allAccounts = accountManagementService.getAllAccounts();
        Assertions.assertEquals(expectedAccounts.size(), allAccounts.size());
        Assertions.assertEquals(expectedAccounts, allAccounts);
    }

    @Test
    public void addAccount_givenNewPin_thenAccountAndClientSuccessfulCreated() {
        Mockito.doReturn(Optional.empty()).when(clientRepository).findById(anyInt());
        Mockito.doReturn(account1).when(accountRepository).save(any());
        Mockito.doReturn(client1).when(clientRepository).save(any());

        Account createdAccount = accountManagementService.createAccount(1234, "Ivanov Ivan");
        Assertions.assertEquals(client1, createdAccount.getClient());
        Assertions.assertEquals(account1.getCurrentBalance(), createdAccount.getCurrentBalance());
    }

    @Test
    public void addAccount_givenExitedPin_thenAccountSuccessfulCreated() {
        Mockito.doReturn(Optional.of(client1)).when(clientRepository).findById(anyInt());
        Mockito.doReturn(account2).when(accountRepository).save(any());

        Account createdAccount = accountManagementService.createAccount(1234, "Ivanov Ivan");
        Assertions.assertEquals(client1, createdAccount.getClient());
        Assertions.assertEquals(account2.getCurrentBalance(), createdAccount.getCurrentBalance());
    }

    @Test
    public void addAccount_givenExceptionFromRepository_thenExceptionThrowingFromMethod() {
        Mockito.doThrow(RuntimeException.class).when(clientRepository).findById(anyInt());
        Assertions.assertThrows(RuntimeException.class,
                () -> accountManagementService.createAccount(1234, "Ivanov Ivan"));
    }
}