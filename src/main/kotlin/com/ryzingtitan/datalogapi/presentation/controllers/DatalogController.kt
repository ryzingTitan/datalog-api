package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.services.LoggingService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api"])
class DatalogController(
    private val loggingService: LoggingService,
) {
    private val logger = LoggerFactory.getLogger(DatalogController::class.java)
}
