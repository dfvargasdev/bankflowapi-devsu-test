package com.devsu.bankflowapi.services;

import com.devsu.bankflowapi.controllers.dto.AccountDTO;
import com.devsu.bankflowapi.models.dto.AccountResponseDTO;


public interface IAccountService {

    AccountResponseDTO createAccount(AccountDTO accountDTO);
    AccountResponseDTO getAccountById(Long accountId);
    AccountResponseDTO updateAccount(Long accountId, AccountDTO accountDTO);
    void deleteAccount(Long accountId);


}
