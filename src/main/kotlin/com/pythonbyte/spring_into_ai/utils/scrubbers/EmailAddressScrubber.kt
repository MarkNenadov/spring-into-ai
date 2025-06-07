package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.springframework.stereotype.Component

@Component
class EmailAddressScrubber : PIIScrubber {
    override fun scrub(str: String): String {
        val emailRegex = Regex(
            """(?x)
            [a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}
            """
        )

        return emailRegex.replace(str) { "[EMAIL REDACTED]" }
    }
} 