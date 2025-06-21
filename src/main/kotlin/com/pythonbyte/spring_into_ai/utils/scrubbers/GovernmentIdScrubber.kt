package com.pythonbyte.spring_into_ai.utils.scrubbers

import org.pythonbyte.krux.privacy.PIIScrubber
import org.springframework.stereotype.Component
import org.pythonbyte.krux.privacy.GovernmentIdScrubber as KruxGovernmentIdScrubber

@Component
class GovernmentIdScrubber : PIIScrubber {
    private val delegate = KruxGovernmentIdScrubber()
    override fun scrub(str: String): String = delegate.scrub(str)
} 