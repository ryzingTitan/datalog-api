package com.ryzingtitan.datalogapi.domain.services

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogapi.domain.dtos.DatalogRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset

@ExperimentalCoroutinesApi
class DatalogRecordServiceTests {
    @BeforeEach
    fun setup() {
        datalogRecordService =
            DatalogRecordService(mockDatalogRecordRepository)
        reset(mockDatalogRecordRepository)
    }

    private lateinit var datalogRecordService: DatalogRecordService

    private val mockDatalogRecordRepository = mock<DatalogRecordRepository>()

    private val datalogRecordEntity = DatalogRecordEntity(
        id = 1,
        username = "testUser",
        firstName = "test",
        lastName = "user"
    )

    private val datalogRecord = DatalogRecord(
        id = 1,
        firstName = "test",
        lastName = "user",
        fullName = "test user"
    )
}
