package com.ryzingtitan.datalogapi.data.datalogrecord.sessionmetadata.repositories

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.sessionmetadata.entities.SessionMetadataEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SessionMetadataRepository : CoroutineCrudRepository<DatalogRecordEntity, UUID> {
    @Aggregation(
        """
            { '${'$'}group': 
                { '_id': '${'$'}sessionId', 
                  'sessionId': { '${'$'}first': '${'$'}sessionId' }, 
                  'startTimeEpochMilliseconds': { '${'$'}min': '${'$'}_id' }, 
                  'endTimeEpochMilliseconds': { '${'$'}max': '${'$'}_id'}
                }
            }
          """,
    )
    fun getAllSessionMetadata(): Flow<SessionMetadataEntity>
}
