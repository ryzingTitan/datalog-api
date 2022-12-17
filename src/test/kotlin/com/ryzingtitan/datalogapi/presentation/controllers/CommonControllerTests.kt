package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.services.DatalogRecordService
import com.ryzingtitan.datalogapi.domain.services.LoggingService
import com.ryzingtitan.datalogapi.presentation.configuration.SecurityConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest
@Import(value = [SecurityConfiguration::class])
@ActiveProfiles("test")
@Suppress("UnnecessaryAbstractClass")
abstract class CommonControllerTests {
    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @MockBean
    protected lateinit var mockLoggingService: LoggingService

    @MockBean
    protected lateinit var mockDatalogRecordService: DatalogRecordService
}
