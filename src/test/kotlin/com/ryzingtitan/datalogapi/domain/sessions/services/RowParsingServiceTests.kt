package com.ryzingtitan.datalogapi.domain.sessions.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.domain.sessions.configuration.ColumnConfiguration
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUploadMetadata
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.Instant

class RowParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `parses the row correctly when it contains valid session data`() =
            runTest {
                val row =
                    "$FIRST_LINE_DEVICE_TIME," +
                        "$FIRST_LINE_LONGITUDE," +
                        "$FIRST_LINE_LATITUDE," +
                        "$FIRST_LINE_ALTITUDE," +
                        "${FIRST_LINE_COOLANT_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_ENGINE_RPM.toFloat()}," +
                        "${FIRST_LINE_INTAKE_AIR_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_SPEED.toFloat()}," +
                        "$FIRST_LINE_THROTTLE_POSITION," +
                        "$FIRST_LINE_BOOST_PRESSURE," +
                        FIRST_LINE_AIR_FUEL_RATIO

                val datalog =
                    rowParsingService.parse(
                        row,
                        fileUploadMetadata,
                        columnConfiguration,
                    )

                assertEquals(sessionId, datalog?.sessionId)
                assertEquals(firstLineTimestamp, datalog?.timestamp)
                assertEquals(FIRST_LINE_LONGITUDE, datalog?.longitude)
                assertEquals(FIRST_LINE_LATITUDE, datalog?.latitude)
                assertEquals(FIRST_LINE_ALTITUDE, datalog?.altitude)
                assertEquals(FIRST_LINE_INTAKE_AIR_TEMPERATURE, datalog?.intakeAirTemperature)
                assertEquals(FIRST_LINE_BOOST_PRESSURE, datalog?.boostPressure)
                assertEquals(FIRST_LINE_COOLANT_TEMPERATURE, datalog?.coolantTemperature)
                assertEquals(FIRST_LINE_ENGINE_RPM, datalog?.engineRpm)
                assertEquals(FIRST_LINE_SPEED, datalog?.speed)
                assertEquals(FIRST_LINE_THROTTLE_POSITION, datalog?.throttlePosition)
                assertEquals(FIRST_LINE_AIR_FUEL_RATIO, datalog?.airFuelRatio)
            }

        @Test
        fun `parses the row correctly when session id is null`() =
            runTest {
                val row =
                    "$FIRST_LINE_DEVICE_TIME," +
                        "$FIRST_LINE_LONGITUDE," +
                        "$FIRST_LINE_LATITUDE," +
                        "$FIRST_LINE_ALTITUDE," +
                        "${FIRST_LINE_COOLANT_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_ENGINE_RPM.toFloat()}," +
                        "${FIRST_LINE_INTAKE_AIR_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_SPEED.toFloat()}," +
                        "$FIRST_LINE_THROTTLE_POSITION," +
                        "$FIRST_LINE_BOOST_PRESSURE," +
                        FIRST_LINE_AIR_FUEL_RATIO

                val datalog =
                    rowParsingService.parse(
                        row,
                        fileUploadMetadata.copy(sessionId = null),
                        columnConfiguration,
                    )

                assertNull(datalog?.sessionId)
                assertEquals(firstLineTimestamp, datalog?.timestamp)
                assertEquals(FIRST_LINE_LONGITUDE, datalog?.longitude)
                assertEquals(FIRST_LINE_LATITUDE, datalog?.latitude)
                assertEquals(FIRST_LINE_ALTITUDE, datalog?.altitude)
                assertEquals(FIRST_LINE_INTAKE_AIR_TEMPERATURE, datalog?.intakeAirTemperature)
                assertEquals(FIRST_LINE_BOOST_PRESSURE, datalog?.boostPressure)
                assertEquals(FIRST_LINE_COOLANT_TEMPERATURE, datalog?.coolantTemperature)
                assertEquals(FIRST_LINE_ENGINE_RPM, datalog?.engineRpm)
                assertEquals(FIRST_LINE_SPEED, datalog?.speed)
                assertEquals(FIRST_LINE_THROTTLE_POSITION, datalog?.throttlePosition)
                assertEquals(FIRST_LINE_AIR_FUEL_RATIO, datalog?.airFuelRatio)
            }

        @Test
        fun `parses the row correctly when air fuel ratio column is missing`() =
            runTest {
                val row =
                    "$FIRST_LINE_DEVICE_TIME," +
                        "$FIRST_LINE_LONGITUDE," +
                        "$FIRST_LINE_LATITUDE," +
                        "$FIRST_LINE_ALTITUDE," +
                        "${FIRST_LINE_COOLANT_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_ENGINE_RPM.toFloat()}," +
                        "${FIRST_LINE_INTAKE_AIR_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_SPEED.toFloat()}," +
                        "$FIRST_LINE_THROTTLE_POSITION," +
                        "$FIRST_LINE_BOOST_PRESSURE,"

                val missingAirFuelRationColumnConfiguration = columnConfiguration.copy(airFuelRatio = -1)

                val datalog =
                    rowParsingService.parse(
                        row,
                        fileUploadMetadata,
                        missingAirFuelRationColumnConfiguration,
                    )

                assertEquals(sessionId, datalog?.sessionId)
                assertEquals(firstLineTimestamp, datalog?.timestamp)
                assertEquals(FIRST_LINE_LONGITUDE, datalog?.longitude)
                assertEquals(FIRST_LINE_LATITUDE, datalog?.latitude)
                assertEquals(FIRST_LINE_ALTITUDE, datalog?.altitude)
                assertEquals(FIRST_LINE_INTAKE_AIR_TEMPERATURE, datalog?.intakeAirTemperature)
                assertEquals(FIRST_LINE_BOOST_PRESSURE, datalog?.boostPressure)
                assertEquals(FIRST_LINE_COOLANT_TEMPERATURE, datalog?.coolantTemperature)
                assertEquals(FIRST_LINE_ENGINE_RPM, datalog?.engineRpm)
                assertEquals(FIRST_LINE_SPEED, datalog?.speed)
                assertEquals(FIRST_LINE_THROTTLE_POSITION, datalog?.throttlePosition)
                assertNull(datalog?.airFuelRatio)
            }

        @Test
        fun `parses the row correctly when it contains invalid session data`() =
            runTest {
                val row =
                    "$SECOND_LINE_DEVICE_TIME," +
                        "$SECOND_LINE_LONGITUDE," +
                        "$SECOND_LINE_LATITUDE," +
                        "$SECOND_LINE_ALTITUDE," +
                        "$SECOND_LINE_COOLANT_TEMPERATURE," +
                        "$SECOND_LINE_ENGINE_RPM," +
                        "$SECOND_LINE_INTAKE_AIR_TEMPERATURE," +
                        "$SECOND_LINE_SPEED," +
                        "$SECOND_LINE_THROTTLE_POSITION," +
                        "$SECOND_LINE_BOOST_PRESSURE," +
                        SECOND_LINE_AIR_FUEL_RATIO

                val datalog =
                    rowParsingService.parse(
                        row,
                        fileUploadMetadata,
                        columnConfiguration,
                    )

                assertEquals(sessionId, datalog?.sessionId)
                assertEquals(secondLineTimestamp, datalog?.timestamp)
                assertEquals(SECOND_LINE_LONGITUDE, datalog?.longitude)
                assertEquals(SECOND_LINE_LATITUDE, datalog?.latitude)
                assertEquals(SECOND_LINE_ALTITUDE, datalog?.altitude)
                assertNull(datalog?.intakeAirTemperature)
                assertNull(datalog?.boostPressure)
                assertNull(datalog?.coolantTemperature)
                assertNull(datalog?.engineRpm)
                assertNull(datalog?.speed)
                assertNull(datalog?.throttlePosition)
                assertNull(datalog?.airFuelRatio)
            }

        @Test
        fun `does not throw error when the row contains unparseable data`() =
            runTest {
                val row =
                    "Device Time," +
                        "$FIRST_LINE_LONGITUDE," +
                        "$FIRST_LINE_LATITUDE," +
                        "$FIRST_LINE_ALTITUDE," +
                        "${FIRST_LINE_COOLANT_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_ENGINE_RPM.toFloat()}," +
                        "${FIRST_LINE_INTAKE_AIR_TEMPERATURE.toFloat()}," +
                        "${FIRST_LINE_SPEED.toFloat()}," +
                        "$FIRST_LINE_THROTTLE_POSITION," +
                        "$FIRST_LINE_BOOST_PRESSURE," +
                        FIRST_LINE_AIR_FUEL_RATIO

                val datalog =
                    rowParsingService.parse(
                        row,
                        fileUploadMetadata,
                        columnConfiguration,
                    )

                assertNull(datalog)

                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals("Unable to parse row: $row", appender.list[0].message)
            }
    }

    @BeforeEach
    fun setup() {
        rowParsingService = RowParsingService()

        logger = LoggerFactory.getLogger(RowParsingService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var rowParsingService: RowParsingService

    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val sessionId = 1
    private val trackId = 3
    private val firstLineTimestamp = Instant.parse("2022-09-18T18:15:47.963Z")
    private val secondLineTimestamp = Instant.parse("2022-09-18T18:18:47.968Z")

    private val fileUploadMetadata =
        FileUploadMetadata(
            fileName = "testFile.txt",
            sessionId = sessionId,
            trackId = trackId,
            userEmail = USER_EMAIL,
            userFirstName = USER_FIRST_NAME,
            userLastName = USER_LAST_NAME,
        )

    private val columnConfiguration =
        ColumnConfiguration(
            deviceTime = 0,
            longitude = 1,
            latitude = 2,
            altitude = 3,
            coolantTemperature = 4,
            engineRpm = 5,
            intakeAirTemperature = 6,
            speed = 7,
            throttlePosition = 8,
            boostPressure = 9,
            airFuelRatio = 10,
        )

    companion object RowParsingServiceTestConstants {
        const val FIRST_LINE_DEVICE_TIME = "18-Sep-2022 14:15:47.963"
        const val FIRST_LINE_LONGITUDE = -86.14162999999999
        const val FIRST_LINE_LATITUDE = 42.406800000000004
        const val FIRST_LINE_ALTITUDE = 188.4f
        const val FIRST_LINE_INTAKE_AIR_TEMPERATURE = 123
        const val FIRST_LINE_BOOST_PRESSURE = 16.5f
        const val FIRST_LINE_COOLANT_TEMPERATURE = 155
        const val FIRST_LINE_ENGINE_RPM = 5500
        const val FIRST_LINE_SPEED = 86
        const val FIRST_LINE_THROTTLE_POSITION = 95.5f
        const val FIRST_LINE_AIR_FUEL_RATIO = 14.7f

        const val SECOND_LINE_DEVICE_TIME = "18-Sep-2022 14:18:47.968"
        const val SECOND_LINE_LONGITUDE = 86.14162999999999
        const val SECOND_LINE_LATITUDE = -42.406800000000004
        const val SECOND_LINE_ALTITUDE = 188.0f
        const val SECOND_LINE_INTAKE_AIR_TEMPERATURE = "-"
        const val SECOND_LINE_BOOST_PRESSURE = "-"
        const val SECOND_LINE_COOLANT_TEMPERATURE = "-"
        const val SECOND_LINE_ENGINE_RPM = "-"
        const val SECOND_LINE_SPEED = "-"
        const val SECOND_LINE_THROTTLE_POSITION = "-"
        const val SECOND_LINE_AIR_FUEL_RATIO = "-"

        const val USER_EMAIL = "test@test.com"
        const val USER_FIRST_NAME = "test"
        const val USER_LAST_NAME = "tester"
    }
}
