package com.pythonbyte.spring_into_ai.services

import com.pythonbyte.spring_into_ai.utils.scrubbers.EmailAddressScrubber
import com.pythonbyte.spring_into_ai.utils.scrubbers.GovernmentIdScrubber
import com.pythonbyte.spring_into_ai.utils.scrubbers.PhoneNumberScrubber
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

    @Mock
    private lateinit var auditService: AuditService

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
        chatService =
            ChatService(
                auditService,
                chatClientBuilder,
                listOf(phoneNumberScrubber, governmentIdScrubber, emailScrubber),
                true,
                100,
                false,
            )
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

        chatService =
            ChatService(
                auditService,
                chatClientBuilder,
                listOf(phoneNumberScrubber, governmentIdScrubber, emailScrubber),
                false,
                100,
                false,
            )
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

        chatService =
            ChatService(
                auditService,
                chatClientBuilder,
                listOf(phoneNumberScrubber, governmentIdScrubber, emailScrubber),
                false,
                100,
                false,
            )

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

        chatService =
            ChatService(
                auditService,
                chatClientBuilder,
                listOf(phoneNumberScrubber, governmentIdScrubber, emailScrubber),
                false,
                100,
                false,
            )
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

    @Test
    fun `prompt should return cached response for repeated prompts`() {
        val input = "What is the weather?"
        val response = "It's sunny!"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)
        `when`(callResponseSpec.content()).thenReturn(response)

        // First call should hit the API
        chatService.prompt(input)
        verify(chatClient, times(1)).prompt(input)

        // Second call should use cache
        chatService.prompt(input)
        verify(chatClient, times(1)).prompt(input) // Still only called once
    }

    @Test
    fun `cache should respect configured size limit`() {
        val cacheSize = 2

        chatService =
            ChatService(
                auditService,
                chatClientBuilder,
                listOf(phoneNumberScrubber, governmentIdScrubber, emailScrubber),
                false,
                cacheSize,
                false,
            )

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)
        `when`(callResponseSpec.content()).thenReturn("response")

        chatService.prompt("prompt1")
        chatService.prompt("prompt2")
        verify(chatClient, times(2)).prompt(anyString())

        chatService.prompt("prompt3")
        verify(chatClient, times(3)).prompt(anyString())

        chatService.prompt("prompt1")
        verify(chatClient, times(4)).prompt(anyString())
    }

    @Test
    fun `prompt should handle null response from chat client`() {
        val input = "What is the weather?"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)
        `when`(callResponseSpec.content()).thenReturn(null)

        val response = chatService.prompt(input)
        assert(response == null)
        verify(chatClient).prompt(input)
    }

    @Test
    fun `prompt should handle empty string response from chat client`() {
        val input = "What is the weather?"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)
        `when`(callResponseSpec.content()).thenReturn("")

        val response = chatService.prompt(input)
        assert(response == "")
        verify(chatClient).prompt(input)
    }

    @Test
    fun `prompt should handle very long response from chat client`() {
        val input = "Generate a long story"
        val longResponse = "Once upon a time...".repeat(1000) // Create a very long response

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)
        `when`(callResponseSpec.content()).thenReturn(longResponse)

        val response = chatService.prompt(input).also {
            verify(chatClient).prompt(input)
        }

        assert(response == longResponse)

        chatService.prompt(input)
        verify(chatClient, times(1)).prompt(input) // Still only called once
    }

    @Test
    fun `prompt should return exact same response object on cache hit`() {
        val input = "What is the weather?"
        val response = "It's sunny!"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)
        `when`(callResponseSpec.content()).thenReturn(response)

        val firstResponse = chatService.prompt(input).also {
            verify(chatClient, times(1)).prompt(input)
        }

        val secondResponse = chatService.prompt(input).also {
            verify(chatClient, times(1)).prompt(input) // Still only called once
        }

        assert(firstResponse === secondResponse)
    }

    @Test
    fun `prompt should make new API call on cache miss`() {
        val input1 = "What is the weather?"
        val input2 = "What is the time?"
        val response1 = "It's sunny!"
        val response2 = "It's 3 PM!"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)
        `when`(callResponseSpec.content()).thenReturn(response1).thenReturn(response2)

        chatService.prompt(input1)
        verify(chatClient, times(1)).prompt(input1)

        chatService.prompt(input2)
        verify(chatClient, times(1)).prompt(input2)
    }

    @Test
    fun `reset should maintain PII scrubbing settings`() {
        val input = "My phone is (555) 123-4567"

        `when`(chatClient.prompt(anyString())).thenReturn(chatClientRequestSpec)
        `when`(chatClientRequestSpec.call()).thenReturn(callResponseSpec)

        chatService.prompt(input)
        verify(chatClient).prompt("My phone is [PHONE NUMBER REDACTED]")

        chatService.reset()
        verify(chatClientBuilder, times(2)).build()

        chatService.prompt(input)
        verify(chatClient, times(2)).prompt("My phone is [PHONE NUMBER REDACTED]")
    }
} 