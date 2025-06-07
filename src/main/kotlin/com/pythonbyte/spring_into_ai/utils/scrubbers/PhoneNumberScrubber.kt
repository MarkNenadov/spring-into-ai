package kotlin.com.pythonbyte.spring_into_ai.utils.scrubbers

import org.springframework.stereotype.Component

@Component
class PhoneNumberScrubber: PIIScrubber {
    override fun scrub(str: String): String {
        val phoneRegex = Regex(
            """(?x)
            (?:
                (?:\+|00)?\d{1,3}[\s\-.\(]?\d{1,4}[\s\-.\)]?\d{1,4}[\s\-.\)]?\d{1,9} |
                \(?\d{3}\)?[\s\-\.]?\d{3}[\s\-\.]?\d{4} |
                \b(1?\d{10})\b
            )
            """
        )

        return phoneRegex.replace(str) { "[PHONE NUMBER REDACTED]" }
    }
}