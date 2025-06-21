package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.pythonbyte.krux.privacy.PIIScrubber
import org.springframework.stereotype.Component
import org.pythonbyte.krux.privacy.EmailAddressScrubber as KruxEmailAddressScrubber

@Component
class EmailAddressScrubber : PIIScrubber {
    private val delegate = KruxEmailAddressScrubber()
    override fun scrub(str: String): String = delegate.scrub(str)
} 