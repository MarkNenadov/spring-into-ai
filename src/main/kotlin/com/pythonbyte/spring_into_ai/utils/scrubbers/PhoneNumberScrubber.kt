package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.springframework.stereotype.Component

@Component
class PhoneNumberScrubber: PIIScrubber {
    override fun scrub(str: String): String {
        val phoneRegex = Regex(
            """(?x)
            (?:
                # International format
                (?:\+|00)?\d{1,3}[\s\-.\(]?\d{1,4}[\s\-.\)]?\d{1,4}[\s\-.\)]?\d{1,9} |
                # US format with area code
                \(?\d{3}\)?[\s\-\.]?\d{3}[\s\-\.]?\d{4} |
                # Plain 10 digits
                \b(?:1?\d{10})\b
            )
            """
        )

        return phoneRegex.replace(str) { matchResult ->
            val number = matchResult.value
            // If it matches SSN format, don't scrub it
            if (number.matches(Regex("""\d{3}[\s\-\.]?\d{2}[\s\-\.]?\d{4}"""))) {
                number
            } else {
                "[PHONE NUMBER REDACTED]"
            }
        }
    }
}