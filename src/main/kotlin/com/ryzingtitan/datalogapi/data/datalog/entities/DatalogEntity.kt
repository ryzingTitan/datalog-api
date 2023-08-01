package com.ryzingtitan.datalogapi.data.datalog.entities

import lombok.Generated
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Generated
@Document("datalog")
data class DatalogEntity(
    @Id val id: String? = null,
    @Indexed val sessionId: UUID,
    @Indexed(direction = IndexDirection.ASCENDING) val epochMilliseconds: Long,
    val data: DataEntity,
    val trackInfo: TrackInfoEntity,
    val user: UserEntity,
)
