package com.luiz.transactions.domain.audit;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_audit_logs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String action;
    
    private String status;

    @Column(name = "from_account_id")
    private UUID fromAccountId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    private String details;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt = Instant.now();

    @PrePersist
    void prePersist() {
        if (occurredAt == null) {
            occurredAt = Instant.now();
        }
    }

    protected AuditLog() {

    }

    public AuditLog(String action, String status, UUID fromAccountId, UUID accountId, String details, String errorMessage) {
        this.action = action;
        this.status = status;
        this.fromAccountId = fromAccountId;
        this.accountId = accountId;
        this.details = details;
        this.errorMessage = errorMessage;
        this.occurredAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public String getStatus() {
        return status;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getDetails() {
        return details;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

}