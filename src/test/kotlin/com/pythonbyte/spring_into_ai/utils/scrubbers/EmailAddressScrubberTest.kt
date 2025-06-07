package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EmailAddressScrubberTest {
    private val scrubber = EmailAddressScrubber()

    @Test
    fun `scrubs basic email addresses`() {
        with("My email is [EMAIL REDACTED].") {
            assertEquals(this, scrubber.scrub("My email is john.doe@example.com."))
            assertEquals(this, scrubber.scrub("My email is jane_doe@example.com."))
            assertEquals(this, scrubber.scrub("My email is user123@example.com."))
        }
    }

    @Test
    fun `scrubs email addresses with subdomains`() {
        with("Contact me at [EMAIL REDACTED].") {
            assertEquals(this, scrubber.scrub("Contact me at user@sub.example.com."))
            assertEquals(this, scrubber.scrub("Contact me at user@mail.sub.example.com."))
        }
    }

    @Test
    fun `scrubs email addresses with special characters`() {
        with("Email: [EMAIL REDACTED]") {
            assertEquals(this, scrubber.scrub("Email: user.name+tag@example.com"))
            assertEquals(this, scrubber.scrub("Email: user-name@example.com"))
        }
    }

    @Test
    fun `does not alter text without email addresses`() {
        val input = "Hello, how are you?"
        val expected = "Hello, how are you?"
        assertEquals(expected, scrubber.scrub(input))
    }

    @Test
    fun `replaces multiple email addresses`() {
        val input = "Contact us at support@example.com or sales@example.com."
        val expected = "Contact us at [EMAIL REDACTED] or [EMAIL REDACTED]."
        assertEquals(expected, scrubber.scrub(input))
    }
} 