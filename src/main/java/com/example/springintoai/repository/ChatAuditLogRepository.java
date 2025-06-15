package com.example.springintoai.repository;

import com.example.springintoai.entity.ChatAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAuditLogRepository extends JpaRepository<ChatAuditLog, Long> {
} 