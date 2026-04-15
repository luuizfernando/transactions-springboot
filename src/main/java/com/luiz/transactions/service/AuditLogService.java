package com.luiz.transactions.service;

import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.luiz.transactions.config.audit.AuditLogFactory;
import com.luiz.transactions.domain.audit.AuditLog;
import com.luiz.transactions.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogFactory auditLogFactory;

    public AuditLogService(AuditLogRepository auditLogRepository, AuditLogFactory auditLogFactory) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogFactory = auditLogFactory;
    }

    @Async
    public void logSuccess(String action, UUID accountId, String details) {
        AuditLog log = auditLogFactory.success(action, accountId, details);
        auditLogRepository.save(log);
    }

    @Async
    public void logFailure(String action, UUID accountId, String details, String errorMessage) {
        AuditLog log = auditLogFactory.failure(action, accountId, details, errorMessage);
        auditLogRepository.save(log);
    }
    
    @Async
    public void logTransaction(UUID fromAccountId, UUID toAccountId, String action, String details) {
        AuditLog log = auditLogFactory.createTransactionLog(fromAccountId, toAccountId, action, details);
        auditLogRepository.save(log);
    }

}