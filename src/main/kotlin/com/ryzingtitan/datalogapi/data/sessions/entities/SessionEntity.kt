package com.ryzingtitan.datalogapi.data.sessions.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("sessions")
data class SessionEntity(
    @Id
    val id: Int? = null,
    val userEmail: String,
    val userFirstName: String,
    val userLastName: String,
    val startTime: Instant,
    val endTime: Instant,
    val trackId: Int,
    val carId: Int,
)
