package com.devsu.bankflowapi.controllers.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class TransactionReportDTO {
    private Date date;
    private String client;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private Boolean status;
    private BigDecimal transaction;
    private BigDecimal availableBalance;
}
