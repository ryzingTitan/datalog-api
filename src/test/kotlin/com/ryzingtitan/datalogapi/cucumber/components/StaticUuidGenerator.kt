package com.ryzingtitan.datalogapi.cucumber.components

import com.ryzingtitan.datalogapi.domain.uuid.UuidGenerator
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Profile("test")
class StaticUuidGenerator : UuidGenerator {
    override fun generate(): UUID {
        return UUID.fromString("c61cc339-f93d-45a4-aa2b-923f0482b97f")
    }
}
