package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PhoneNumberScrubberTest {
    private val scrubber = PhoneNumberScrubber()

    @Test
    fun `scrubs a variety of phone number formats`() {
        with("My phone is [PHONE NUMBER REDACTED].") {
            assertEquals(this, scrubber.scrub("My phone is 5551234567."))
            assertEquals(this, scrubber.scrub("My phone is 15551234567."))
            assertEquals(this, scrubber.scrub("My phone is 555-123-4567."))
            assertEquals(this, scrubber.scrub("My phone is (555) 123-4567."))
            assertEquals(this, scrubber.scrub("My phone is 0044 20 7946 0958."))
            assertEquals(this, scrubber.scrub("My phone is +1 555 123 4567."))
        }
    }

    @Test
    fun `does not alter text without phone numbers`() {
        val input = "Hello, how are you?"
        val expected = "Hello, how are you?"
        assertEquals(expected, scrubber.scrub(input))
    }

    @Test
    fun `replaces multiple phone numbers`() {
        val input = "Call 555-123-4567 or (555) 987-6543."
        val expected = "Call [PHONE NUMBER REDACTED] or [PHONE NUMBER REDACTED]."
        assertEquals(expected, scrubber.scrub(input))
    }
}