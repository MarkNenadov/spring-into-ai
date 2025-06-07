package com.pythonbyte.spring_into_ai.services

import com.pythonbyte.spring_into_ai.utils.scrubbers.PhoneNumberScrubber
import com.pythonbyte.spring_into_ai.utils.scrubbers.GovernmentIdScrubber
import com.pythonbyte.spring_into_ai.utils.scrubbers.EmailAddressScrubber
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.ai.chat.client.ChatClient

@ExtendWith(MockitoExtension::class)
class ChatServiceTest {

    @Mock
    private lateinit var chatClientBuilder: ChatClient.Builder

    @Mock
    private lateinit var chatClient: ChatClient

    @Mock
    private lateinit var chatClientRequestSpec: ChatClient.ChatClientRequestSpec

    @Mock
    private lateinit var callResponseSpec: ChatClient.CallResponseSpec

    private lateinit var phoneNumberScrubber: PhoneNumberScrubber
    private lateinit var governmentIdScrubber: GovernmentIdScrubber
    private lateinit var emailScrubber: EmailAddressScrubber
    private lateinit var chatService: ChatService

    @BeforeEach
    fun setup() {
        `when`(chatClientBuilder.build()).thenReturn(chatClient)
        phoneNumberScrubber = PhoneNumberScrubber()
        governmentIdScrubber = GovernmentIdScrubber()
        emailScrubber = EmailAddressScrubber()
        chatService = ChatService(chatClientBuilder, listOf(phoneNumberScrubber, governmentIdScrubber, emailScrubber), true)
    }

    @Test
    fun `prompt should redact phone numbers when PII scrubbing is enabled`() {
        val input = "Hello, my phone number is (555) 123-4567"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

        chatService.prompt(input).also {
            verify(chatClient).prompt("Hello, my phone number is [PHONE NUMBER REDACTED]")
        }
    }

    @Test
    fun `prompt should not redact phone numbers when PII scrubbing is disabled`() {
        val input = "Hello, my phone number is (555) 123-4567"

        chatService = ChatService(chatClientBuilder, listOf(phoneNumberScrubber), false)
        `when`(chatClient.prompt(input)).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

        chatService.prompt(input).also {
            verify(chatClient).prompt(input)
        }
    }

    @Test
    fun `prompt should handle multiple phone numbers in text`() {
        val input = "Call me at 555-123-4567 or (555) 987-6543"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

       chatService.prompt(input).also {
           verify(chatClient).prompt("Call me at [PHONE NUMBER REDACTED] or [PHONE NUMBER REDACTED]")
       }
    }

    @Test
    fun `prompt should redact government IDs when PII scrubbing is enabled`() {
        val input = "My SSN is 123-45-6789"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

        chatService.prompt(input).also {
            verify(chatClient).prompt("My SSN is [SSN REDACTED]")
        }
    }

    @Test
    fun `prompt should not redact government IDs when PII scrubbing is disabled`() {
        val input = "My SSN is 123-45-6789"

        chatService = ChatService(chatClientBuilder, listOf(phoneNumberScrubber, governmentIdScrubber), false)
        `when`(chatClient.prompt(input)).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

        chatService.prompt(input).also {
            verify(chatClient).prompt(input)
        }
    }

    @Test
    fun `prompt should redact email addresses when PII scrubbing is enabled`() {
        val input = "Contact me at john.doe@example.com"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

        chatService.prompt(input).also {
            verify(chatClient).prompt("Contact me at [EMAIL REDACTED]")
        }
    }

    @Test
    fun `prompt should not redact email addresses when PII scrubbing is disabled`() {
        val input = "Contact me at john.doe@example.com"

        chatService = ChatService(chatClientBuilder, listOf(phoneNumberScrubber, governmentIdScrubber, emailScrubber), false)
        `when`(chatClient.prompt(input)).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

        chatService.prompt(input).also {
            verify(chatClient).prompt(input)
        }
    }

    @Test
    fun `reset should rebuild chat client`() {
        chatService.reset().also {
            verify(chatClientBuilder, times(2)).build()
        }
    }
} 