package com.pythonbyte.spring_into_ai

import com.pythonbyte.spring_into_ai.services.ChatService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringIntoAiApplicationTest {
    @Autowired
    private lateinit var chatService: ChatService

    @Test
    @Disabled("Disabled in CI due to external API dependency")
    fun test() {
        val response = chatService.prompt("What is the weather like in Windsor, Ontario?")
        println(response)
    }
}