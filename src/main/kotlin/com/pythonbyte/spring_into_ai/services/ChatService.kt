package com.pythonbyte.spring_into_ai.services

import org.springframework.ai.chat.client.ChatClient
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class ChatService(private val builder: ChatClient.Builder) {
    private var chatClient = builder.build()

    fun prompt(str: String): String? {
        var response: String? = null
        CommandLineRunner {
            response = chatClient.prompt(str).call().content()
        }.run()

        return response
    }

    fun reset() {
        this.chatClient = builder.build()
    }
}