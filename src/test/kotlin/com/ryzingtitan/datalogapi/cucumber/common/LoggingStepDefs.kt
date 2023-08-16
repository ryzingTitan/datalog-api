package com.ryzingtitan.datalogapi.cucumber.common

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.cucumber.dtos.LogMessage
import com.ryzingtitan.datalogapi.domain.session.services.FileParsingService
import com.ryzingtitan.datalogapi.domain.session.services.SessionService
import com.ryzingtitan.datalogapi.domain.track.services.TrackService
import com.ryzingtitan.datalogapi.presentation.controllers.DatalogController
import com.ryzingtitan.datalogapi.presentation.controllers.SessionMetadataController
import io.cucumber.datatable.DataTable
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import org.slf4j.LoggerFactory

class LoggingStepDefs {
    @DataTableType
    fun mapLogMessage(tableRow: Map<String, String>): LogMessage {
        return LogMessage(
            level = tableRow["level"].toString(),
            message = tableRow["message"].toString(),
        )
    }

    @Before
    fun setup() {
        sessionMetadataControllerLogger = LoggerFactory.getLogger(SessionMetadataController::class.java) as Logger
        sessionMetadataControllerLogger.addAppender(appender)

        datalogControllerLogger = LoggerFactory.getLogger(DatalogController::class.java) as Logger
        datalogControllerLogger.addAppender(appender)

        fileParsingServiceLogger = LoggerFactory.getLogger(FileParsingService::class.java) as Logger
        fileParsingServiceLogger.addAppender(appender)

        sessionServiceLogger = LoggerFactory.getLogger(SessionService::class.java) as Logger
        sessionServiceLogger.addAppender(appender)

        trackServiceLogger = LoggerFactory.getLogger(TrackService::class.java) as Logger
        trackServiceLogger.addAppender(appender)

        appender.context = LoggerContext()
        appender.start()
    }

    @Then("the application will log the following messages:")
    fun theApplicationWilLogTheFollowingMessages(table: DataTable) {
        val expectedLogMessages: List<LogMessage> = table.tableConverter.toList(table, LogMessage::class.java)

        val actualLogMessages = ArrayList<LogMessage>()

        appender.list.forEach {
            actualLogMessages.add(LogMessage(it.level.levelStr, it.message))
        }

        assertEquals(expectedLogMessages.sortedBy { it.message }, actualLogMessages.sortedBy { it.message })
    }

    @After
    fun teardown() {
        appender.stop()
    }

    private lateinit var datalogControllerLogger: Logger
    private lateinit var sessionMetadataControllerLogger: Logger
    private lateinit var fileParsingServiceLogger: Logger
    private lateinit var sessionServiceLogger: Logger
    private lateinit var trackServiceLogger: Logger

    private val appender: ListAppender<ILoggingEvent> = ListAppender()
}
