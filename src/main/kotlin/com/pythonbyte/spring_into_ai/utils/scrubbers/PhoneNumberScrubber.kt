package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.pythonbyte.krux.privacy.PIIScrubber
import org.springframework.stereotype.Component
import org.pythonbyte.krux.privacy.PhoneNumberScrubber as KruxPhoneNumberScrubber

@Component
class PhoneNumberScrubber : PIIScrubber {
    private val delegate = KruxPhoneNumberScrubber()
    override fun scrub(str: String): String = delegate.scrub(str)
}