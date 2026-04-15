package com.luiz.transactions.config.audit;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.luiz.transactions.domain.audit.AuditLog;

@Component
public class AuditLogFactory {
    
    public AuditLog success(String action, UUID accountId, String details) {
        return new AuditLog(action.toUpperCase(), "SUCCESS", null, accountId, details, null);
    }

    public AuditLog success(String action, UUID fromAccountId, UUID accountId, String details) {
        return new AuditLog(action.toUpperCase(), "SUCCESS", fromAccountId, accountId, details, null);
    }

    public AuditLog failure(String action, UUID accountId, String details, String errorMessage) {
        return new AuditLog(action.toUpperCase(), "FAILURE", null, accountId, details, errorMessage);
    }

    public AuditLog createTransactionLog(UUID fromAccountId, UUID toAccountId, String action, String details) {
        return new AuditLog(action.toUpperCase(), "SUCCESS", fromAccountId, toAccountId, details, null);
    }

}