package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GovernmentIdScrubberTest {
    private val scrubber = GovernmentIdScrubber()

    @Test
    fun `scrubs SIN numbers in various formats`() {
        with("My SIN is [SIN REDACTED].") {
            assertEquals(this, scrubber.scrub("My SIN is 123456789."))
            assertEquals(this, scrubber.scrub("My SIN is 123-456-789."))
            assertEquals(this, scrubber.scrub("My SIN is 123 456 789."))
        }
    }

    @Test
    fun `scrubs SSN numbers in various formats`() {
        with("My SSN is [SSN REDACTED].") {
            assertEquals(this, scrubber.scrub("My SSN is 123-45-6789."))
            assertEquals(this, scrubber.scrub("My SSN is 123 45 6789."))
            assertEquals(this, scrubber.scrub("My SSN is 123456789."))
        }
    }

    @Test
    fun `identifies SSN by invalid first digit`() {
        with("My SSN is [SSN REDACTED].") {
            assertEquals(this, scrubber.scrub("My SSN is 000-45-6789."))
            assertEquals(this, scrubber.scrub("My SSN is 800-45-6789."))
            assertEquals(this, scrubber.scrub("My SSN is 900456789."))
        }
    }

    @Test
    fun `does not alter text without government IDs`() {
        val input = "Hello, how are you?"
        val expected = "Hello, how are you?"
        assertEquals(expected, scrubber.scrub(input))
    }

    @Test
    fun `replaces multiple government IDs`() {
        val input = "My SIN is 123-456-789 and SSN is 000-45-6789."
        val expected = "My SIN is [SIN REDACTED] and SSN is [SSN REDACTED]."
        assertEquals(expected, scrubber.scrub(input))
    }
} 