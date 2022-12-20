package com.ryzingtitan.datalogapi.cucumber.common

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.cucumber.dtos.LogMessage
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
            message = tableRow["message"].toString()
        )
    }

    @Before
    fun setup() {
        userControllerLogger = LoggerFactory.getLogger(SessionController::class.java) as Logger
        userControllerLogger.addAppender(appender)

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

    private lateinit var userControllerLogger: Logger

    private val appender: ListAppender<ILoggingEvent> = ListAppender()
}
