package com.devsu.bankflowapi.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    WITHDRAWAL("Retiro"),
    DEPOSIT("Depósito");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }
}
