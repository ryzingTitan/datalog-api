package com.ryzingtitan.datalogapi.domain.uuid

import java.util.UUID

interface UuidGenerator {
    fun generate(): UUID
}
