package com.luiz.transactions.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.transactions.domain.audit.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

}