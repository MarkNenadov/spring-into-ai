package com.pythonbyte.spring_into_ai.services

import org.pythonbyte.krux.privacy.PIIScrubber
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service
import java.util.LinkedHashMap

@Service
class ChatService(
    private val auditService: AuditService,
    private val builder: ChatClient.Builder,
    private val piiScrubbers: List<PIIScrubber>,
    @Value("\${chat.service.scrub.pii}") private val scrubPii: Boolean,
    @Value("\${chat.service.cache.size:100}") private val cacheSize: Int,
    @Value("\${chat.service.auditing.enabled}") private val auditingEnabled: Boolean,
) {
    private var chatClient = builder.build()
    
    // Cache with configurable maximum size using LinkedHashMap
    private val cache = object : LinkedHashMap<String, String>(cacheSize, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?): Boolean {
            return size > cacheSize
        }
    }

    fun prompt(str: String): String? =
        with(scrubText(str)) {
            println(">> $this")
            
            // Check cache first
            cache[this]?.let { return it }
            
            var response: String? = null
            CommandLineRunner {
                response = chatClient.prompt(this).call().content()
            }.run()

            response?.let { cache[this] = it }

            auditChat(str, response)

            return response
        }

    private fun auditChat(prompt: String, response: String?) {
        if (auditingEnabled) {
            auditService.storeChatAuditLog(prompt, response ?: "")
        }
    }

    private fun scrubText(str: String) =
        if (scrubPii) {
            piiScrubbers.fold(str) { acc, scrubber -> scrubber.scrub(acc) }
        } else {
            str
        }

    fun reset() {
        this.chatClient = builder.build()
    }
}