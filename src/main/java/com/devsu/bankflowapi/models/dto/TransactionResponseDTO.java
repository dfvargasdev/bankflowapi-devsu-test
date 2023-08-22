package com.devsu.bankflowapi.models.dto;

import com.devsu.bankflowapi.models.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;
    private Date date;
    private String transactionType;
    private BigDecimal value;
    private BigDecimal balance;
    private String transaction;
    private Account account;
}
