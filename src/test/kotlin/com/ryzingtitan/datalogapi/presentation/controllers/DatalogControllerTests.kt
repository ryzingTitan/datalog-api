package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.dtos.DatalogRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.reset

@ExperimentalCoroutinesApi
class DatalogControllerTests : CommonControllerTests() {
    @BeforeEach
    fun setup() {
        reset(mockLoggingService, mockDatalogRecordService)
    }

    private val firstDatalogRecord = DatalogRecord(
        id = 1,
        firstName = FIRST_NAME,
        lastName = LAST_NAME,
        fullName = "$FIRST_NAME $LAST_NAME"
    )

    private val secondDatalogRecord = DatalogRecord(
        id = 2,
        firstName = FIRST_NAME,
        lastName = LAST_NAME,
        fullName = "$FIRST_NAME $LAST_NAME"
    )

    companion object UserControllerTestConstants {
        private const val FIRST_NAME = "happy"
        private const val LAST_NAME = "carson"
    }
}
