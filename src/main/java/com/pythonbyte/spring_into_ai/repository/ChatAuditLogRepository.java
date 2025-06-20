package com.pythonbyte.spring_into_ai.repository;

import com.pythonbyte.spring_into_ai.entity.ChatAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAuditLogRepository extends JpaRepository<ChatAuditLog, Long> {
} 