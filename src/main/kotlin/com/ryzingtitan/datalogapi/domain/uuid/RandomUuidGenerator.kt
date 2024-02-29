package com.ryzingtitan.datalogapi.domain.uuid

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Profile("production", "local")
class RandomUuidGenerator : UuidGenerator {
    override fun generate(): UUID {
        return UUID.randomUUID()
    }
}
