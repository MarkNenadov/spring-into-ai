package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.springframework.stereotype.Component

@Component
class GovernmentIdScrubber : PIIScrubber {
    override fun scrub(str: String): String {
        // Generic patterns (now explicitly including plain 9-digits for both)
        val sinGenericRegex = Regex("\\b(\\d{3}[\\s-]?\\d{3}[\\s-]?\\d{3}|\\d{9})\\b")
        val ssnGenericRegex = Regex("\\b(\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{4}|\\d{9})\\b")

        // Context-aware patterns for explicit "SSN is" or "SIN is"
        val ssnContextRegex = Regex("(?i)(SSN\\s+is\\s+)(\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{4}|\\d{9})")
        val sinContextRegex = Regex("(?i)(SIN\\s+is\\s+)(\\d{3}[\\s-]?\\d{3}[\\s-]?\\d{3}|\\d{9})")

        var result = str

        // Prioritize context-aware replacements
        // The context regex captures the prefix (e.g., "SSN is ") in group 1
        result = ssnContextRegex.replace(result) { matchResult ->
            val prefix = matchResult.groupValues[1]
            "$prefix[SSN REDACTED]"
        }

        result = sinContextRegex.replace(result) { matchResult ->
            val prefix = matchResult.groupValues[1]
            "$prefix[SIN REDACTED]"
        }
        
        // Then apply generic patterns to any remaining numbers
        // SSN generic first to handle plain 9-digits as SSN by default if not caught by context
        result = ssnGenericRegex.replace(result) { matchResult ->
            val number = matchResult.groupValues[1]
            if (number.length == 9 && (number.startsWith("0") || number.startsWith("8") || number.startsWith("9"))) {
                "[SSN REDACTED]"
            } else if (number.length == 9) {
                "[SIN REDACTED]"
            } else if (number.matches(Regex("\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{4}"))) {
                "[SSN REDACTED]"
            } else if (number.matches(Regex("\\d{3}[\\s-]?\\d{3}[\\s-]?\\d{3}"))) {
                "[SIN REDACTED]"
            } else {
                "[GOVERNMENT ID REDACTED]"
            }
        }

        // Fallback for any SINs not covered by the above
        result = sinGenericRegex.replace(result) { matchResult ->
            val number = matchResult.groupValues[1]
            if (number.length == 9 && !(number.startsWith("0") || number.startsWith("8") || number.startsWith("9"))) {
                "[SIN REDACTED]"
            } else if (number.matches(Regex("\\d{3}[\\s-]?\\d{3}[\\s-]?\\d{3}"))) {
                "[SIN REDACTED]"
            } else {
                "[GOVERNMENT ID REDACTED]"
            }
        }

        return result
    }
} 