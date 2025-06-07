package com.pythonbyte.spring_into_ai.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_audit_logs")
data class ChatAuditLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var prompt: String = "",

    @Column(nullable = false)
    var response: String = "",

    @Column(name = "created_date", nullable = false)
    val createdDate: LocalDateTime = LocalDateTime.now()
)