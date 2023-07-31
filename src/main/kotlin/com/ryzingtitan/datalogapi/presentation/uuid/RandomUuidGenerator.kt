package com.ryzingtitan.datalogapi.presentation.uuid

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
@Profile("production")
class RandomUuidGenerator : UuidGenerator {
    override fun generate(): UUID {
        return UUID.randomUUID()
    }
}
