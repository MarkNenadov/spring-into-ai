package com.pythonbyte.spring_into_ai.services

import com.pythonbyte.spring_into_ai.utils.scrubbers.PIIScrubber
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val builder: ChatClient.Builder,
    private val piiScrubbers: List<PIIScrubber>,
    @Value("\${chat.service.scrub.pii}") private val scrubPii: Boolean
) {
    private var chatClient = builder.build()

    fun prompt(str: String): String? =
        with(scrubText(str)) {
            println( ">> $this")
            var response: String? = null
            CommandLineRunner {
                response = chatClient.prompt(this).call().content()
            }.run()

            return response
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