package com.ryzingtitan.datalogapi.cucumber.common

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.cucumber.dtos.LogMessage
import com.ryzingtitan.datalogapi.domain.sessions.services.FileParsingService
import com.ryzingtitan.datalogapi.domain.sessions.services.RowParsingService
import com.ryzingtitan.datalogapi.domain.sessions.services.SessionService
import com.ryzingtitan.datalogapi.domain.tracks.services.TrackService
import com.ryzingtitan.datalogapi.presentation.controllers.DatalogController
import com.ryzingtitan.datalogapi.presentation.controllers.SessionController
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
        sessionControllerLogger = LoggerFactory.getLogger(SessionController::class.java) as Logger
        sessionControllerLogger.addAppender(appender)

        datalogControllerLogger = LoggerFactory.getLogger(DatalogController::class.java) as Logger
        datalogControllerLogger.addAppender(appender)

        fileParsingServiceLogger = LoggerFactory.getLogger(FileParsingService::class.java) as Logger
        fileParsingServiceLogger.addAppender(appender)

        sessionServiceLogger = LoggerFactory.getLogger(SessionService::class.java) as Logger
        sessionServiceLogger.addAppender(appender)

        trackServiceLogger = LoggerFactory.getLogger(TrackService::class.java) as Logger
        trackServiceLogger.addAppender(appender)

        rowParsingServiceLogger = LoggerFactory.getLogger(RowParsingService::class.java) as Logger
        rowParsingServiceLogger.addAppender(appender)

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

        assertEquals(expectedLogMessages, actualLogMessages)
    }

    @After
    fun teardown() {
        appender.stop()
    }

    private lateinit var datalogControllerLogger: Logger
    private lateinit var sessionControllerLogger: Logger
    private lateinit var fileParsingServiceLogger: Logger
    private lateinit var sessionServiceLogger: Logger
    private lateinit var trackServiceLogger: Logger
    private lateinit var rowParsingServiceLogger: Logger

    private val appender: ListAppender<ILoggingEvent> = ListAppender()
}
