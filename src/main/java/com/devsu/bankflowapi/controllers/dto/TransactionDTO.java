package com.devsu.bankflowapi.controllers.dto;

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
public class TransactionDTO {

    private Date date;
    private String transactionType;
    private BigDecimal value;
    private BigDecimal balance;
    private Long accountId;
}
