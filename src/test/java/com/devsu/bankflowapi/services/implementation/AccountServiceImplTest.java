package com.devsu.bankflowapi.services.implementation;

import com.devsu.bankflowapi.controllers.dto.AccountDTO;
import com.devsu.bankflowapi.models.Account;
import com.devsu.bankflowapi.models.Client;
import com.devsu.bankflowapi.repositories.IAccountRepository;
import com.devsu.bankflowapi.repositories.IClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks private AccountServiceImpl accountServiceImpl;
    @Mock private IAccountRepository accountRepository;
    @Mock private IClientRepository clientRepository;

    AccountDTO accountDTO = new AccountDTO();
    Account account = new Account();

    @BeforeEach
    public void setUp(){
        accountDTO.setAccountNumber("12");
        accountDTO.setAccountType("1");
        accountDTO.setStatus(true);
        accountDTO.setInitialBalance(BigDecimal.ONE);
        accountDTO.setClientId(12L);

        account.setClient(new Client());
        account.setAccountNumber("12");
        account.setAccountType("2");
        account.setId(3L);
        account.setStatus(true);
        account.setInitialBalance(BigDecimal.ONE);
    }

    @Test
    void createAccount() {
        Mockito.when(accountRepository.save(Mockito.any()))
                .thenReturn(account);

        var result = accountServiceImpl.createAccount(accountDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    void getAccountById() {
        Mockito.when(accountRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(account));

        var result = accountServiceImpl.getAccountById(1L);
        Assertions.assertNotNull(result);
    }

    @Test
    void updateAccount() {
        Mockito.when(accountRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(account));

        Mockito.when(accountRepository.save(Mockito.any()))
                .thenReturn(account);

        var result = accountServiceImpl.updateAccount(1L, accountDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    void deleteAccount() {
        Mockito.when(accountRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(account));

        accountServiceImpl.deleteAccount(1L);
        Mockito.verify(accountRepository, Mockito.times(1)).delete(account);
    }
}