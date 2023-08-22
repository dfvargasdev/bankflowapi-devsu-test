package com.devsu.bankflowapi.controllers;

import com.devsu.bankflowapi.controllers.dto.AccountDTO;
import com.devsu.bankflowapi.models.Account;
import com.devsu.bankflowapi.models.Client;
import com.devsu.bankflowapi.models.dto.AccountResponseDTO;
import com.devsu.bankflowapi.services.IAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private IAccountService accountService;

    AccountDTO accountDTO = new AccountDTO();
    AccountResponseDTO responseDTO = new AccountResponseDTO();

    @BeforeEach
    public void setUp(){
        accountDTO.setAccountNumber("12");
        accountDTO.setAccountType("1");
        accountDTO.setStatus(true);
        accountDTO.setInitialBalance(BigDecimal.ONE);
        accountDTO.setClientId(12L);
    }

    @Test
    void createAccount() {
        Mockito.when(accountService.createAccount(Mockito.any())).thenReturn(responseDTO);

        var result = accountController.createAccount(accountDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    void getAccountById() {
        Mockito.when(accountService.getAccountById(Mockito.any())).thenReturn(responseDTO);

        var result = accountController.getAccountById(1L);
        Assertions.assertNotNull(result);

    }

    @Test
    void updateAccount() {
        Mockito.when(accountService.updateAccount(Mockito.any(), Mockito.any())).thenReturn(responseDTO);

        var result = accountController.updateAccount(1L, accountDTO);
    }

    @Test
    void updatePatchAccount() {
        Mockito.when(accountService.updateAccount(Mockito.any(), Mockito.any())).thenReturn(responseDTO);

        var result = accountController.updateAccount(1L, accountDTO);
    }

    @Test
    void deleteAccount() {
        Mockito.doNothing().when(accountService).deleteAccount(Mockito.any());

        accountController.deleteAccount(1L);
        Mockito.verify(accountService, Mockito.times(1)).deleteAccount(1L);
    }
}