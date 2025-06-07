package com.pythonbyte.spring_into_ai.repositories

import com.pythonbyte.spring_into_ai.entities.ChatAuditLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatAuditLogRepository : JpaRepository<ChatAuditLog, Long> 