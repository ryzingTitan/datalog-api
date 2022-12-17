package com.ryzingtitan.datalogapi.domain.dtos

import lombok.Generated

@Generated
data class DatalogRecord(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val fullName: String
)
