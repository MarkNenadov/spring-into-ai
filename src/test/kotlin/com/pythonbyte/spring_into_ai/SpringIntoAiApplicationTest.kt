package com.pythonbyte.spring_into_ai

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.pythonbyte.spring_into_ai.services.ChatService

@SpringBootTest
class SpringIntoAiApplicationTest {
    @Autowired
    private lateinit var chatService: ChatService

    @Test
    fun test() {
        val response = chatService.prompt("What is the weather like in Windsor, Ontario?")
        println(response)
    }
}