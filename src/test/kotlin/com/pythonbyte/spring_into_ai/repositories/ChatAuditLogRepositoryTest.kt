package com.pythonbyte.spring_into_ai.repositories

import com.pythonbyte.spring_into_ai.entities.ChatAuditLog
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class ChatAuditLogRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var repository: ChatAuditLogRepository

    @Test
    fun `should save and retrieve chat audit log`() {
        val auditLog = ChatAuditLog(
            prompt = "Test prompt",
            response = "Test response"
        )

        val savedLog = repository.save(auditLog)
        val retrievedLog = repository.findById(savedLog.id!!).orElse(null)

        assertThat(retrievedLog).isNotNull
        assertThat(retrievedLog?.prompt).isEqualTo("Test prompt")
        assertThat(retrievedLog?.response).isEqualTo("Test response")
        assertThat(retrievedLog?.createdDate).isNotNull
    }

    @Test
    fun `should update chat audit log`() {
        val auditLog = ChatAuditLog(
            prompt = "Original prompt",
            response = "Original response"
        )
        val savedLog = repository.save(auditLog)

        savedLog.response = "Updated response"
        repository.save(savedLog)
        entityManager.flush()
        entityManager.clear()

        val updatedLog = repository.findById(savedLog.id!!).orElse(null)
        assertThat(updatedLog?.response).isEqualTo("Updated response")
    }

    @Test
    fun `should delete chat audit log`() {
        val auditLog = ChatAuditLog(
            prompt = "Test prompt",
            response = "Test response"
        )
        val savedLog = repository.save(auditLog)

        repository.delete(savedLog)
        entityManager.flush()
        entityManager.clear()

        val result = repository.findById(savedLog.id!!).orElse(null)
        assertThat(result).isNull()
    }

    @Test
    fun `should find all chat audit logs`() {
        val auditLog1 = ChatAuditLog(
            prompt = "Prompt 1",
            response = "Response 1"
        )
        val auditLog2 = ChatAuditLog(
            prompt = "Prompt 2",
            response = "Response 2"
        )
        repository.save(auditLog1)
        repository.save(auditLog2)

        val allLogs = repository.findAll()

        assertThat(allLogs).hasSize(2)
        assertThat(allLogs.map { it.prompt }).containsExactlyInAnyOrder("Prompt 1", "Prompt 2")
    }
} 