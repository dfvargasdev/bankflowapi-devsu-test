package com.devsu.bankflowapi.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDTO {

    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private Boolean status;
    private Long clientId;

}
