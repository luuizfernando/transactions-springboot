package com.luiz.transactions.domain.transaction.enums;

public enum TransactionCategory {
 
    ALIMENTACAO("ALIMENTACAO"),
    TRANSPORTE("TRANSPORTE"),
    LAZER("LAZER"),
    MORADIA("MORADIA"),
    OUTROS("OUTROS");
    
    private final String value;

    TransactionCategory(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
}