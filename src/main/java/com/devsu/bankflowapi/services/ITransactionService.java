package com.devsu.bankflowapi.services;

import com.devsu.bankflowapi.controllers.dto.TransactionDTO;
import com.devsu.bankflowapi.models.dto.TransactionResponseDTO;

public interface ITransactionService {
    TransactionResponseDTO createTransaction(TransactionDTO transactionDTO);

    TransactionResponseDTO getTransactionById(Long transactionId);

    TransactionResponseDTO updateTransaction(Long transactionId, TransactionDTO transactionDTO);

    void deleteTransaction(Long transactionId);
}
