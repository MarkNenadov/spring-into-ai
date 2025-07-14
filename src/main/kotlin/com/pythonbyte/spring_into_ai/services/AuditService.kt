package com.pythonbyte.spring_into_ai.services

import com.pythonbyte.spring_into_ai.entities.ChatAuditLog
import com.pythonbyte.spring_into_ai.repositories.ChatAuditLogRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuditService(private val chatAuditLogRepository: ChatAuditLogRepository) {
    fun storeChatAuditLog(prompt: String, response: String) {
        require(prompt.isNotEmpty()) {
            "Attempting to audit chat but prompt is empty"
        }
        require(!response.isNotEmpty()) {
            "Attempting to audit chat but response is empty"
        }

        chatAuditLogRepository.save(
            ChatAuditLog(
                prompt = prompt,
                response = response,
                createdDate = LocalDateTime.now(),
            )
        )
    }
}