package com.ryzingtitan.datalogapi.data.sessionmetadata.repositories

import com.ryzingtitan.datalogapi.data.datalog.entities.DatalogEntity
import com.ryzingtitan.datalogapi.data.sessionmetadata.entities.SessionMetadataEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SessionMetadataRepository : CoroutineCrudRepository<DatalogEntity, UUID> {
    @Aggregation(
        """
            { '${'$'}group': 
                { '_id': '${'$'}sessionId', 
                  'sessionId': { '${'$'}first': '${'$'}sessionId' }, 
                  'username': { '${'$'}first': '${'$'}user.email' }, 
                  'startTimeEpochMilliseconds': { '${'$'}min': '${'$'}_id' }, 
                  'endTimeEpochMilliseconds': { '${'$'}max': '${'$'}_id'}
                }
            }
          """,
    )
    fun getAllSessionMetadata(): Flow<SessionMetadataEntity>
}
