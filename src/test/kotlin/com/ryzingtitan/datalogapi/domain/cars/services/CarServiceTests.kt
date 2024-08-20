package com.ryzingtitan.datalogapi.domain.cars.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.data.cars.entities.CarEntity
import com.ryzingtitan.datalogapi.data.cars.repositories.CarRepository
import com.ryzingtitan.datalogapi.domain.cars.dtos.Car
import com.ryzingtitan.datalogapi.domain.cars.exceptions.CarAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.cars.exceptions.CarDoesNotExistException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory

class CarServiceTests {
    @Nested
    inner class Create {
        @Test
        fun `creates a new car`() =
            runTest {
                whenever(
                    mockCarRepository
                        .findByYearManufacturedAndMakeAndModel(FIRST_CAR_YEAR, FIRST_CAR_MAKE, FIRST_CAR_MODEL),
                ).thenReturn(null)
                whenever(mockCarRepository.save(firstCarEntity.copy(id = null))).thenReturn(firstCarEntity)

                val carId = carService.create(firstCar.copy(id = null))

                assertEquals(FIRST_CAR_ID, carId)
                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals(
                    "Created car $FIRST_CAR_YEAR $FIRST_CAR_MAKE $FIRST_CAR_MODEL",
                    appender.list[0].message,
                )

                verify(mockCarRepository, times(1))
                    .findByYearManufacturedAndMakeAndModel(FIRST_CAR_YEAR, FIRST_CAR_MAKE, FIRST_CAR_MODEL)
                verify(mockCarRepository, times(1)).save(firstCarEntity.copy(id = null))
            }

        @Test
        fun `does not create a duplicate car`() =
            runTest {
                whenever(
                    mockCarRepository
                        .findByYearManufacturedAndMakeAndModel(FIRST_CAR_YEAR, FIRST_CAR_MAKE, FIRST_CAR_MODEL),
                ).thenReturn(firstCarEntity)

                val exception =
                    assertThrows<CarAlreadyExistsException> {
                        carService.create(firstCar.copy(id = null))
                    }

                assertEquals("$FIRST_CAR_YEAR $FIRST_CAR_MAKE $FIRST_CAR_MODEL already exists", exception.message)
                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals(
                    "$FIRST_CAR_YEAR $FIRST_CAR_MAKE $FIRST_CAR_MODEL already exists",
                    appender.list[0].message,
                )

                verify(mockCarRepository, times(1))
                    .findByYearManufacturedAndMakeAndModel(FIRST_CAR_YEAR, FIRST_CAR_MAKE, FIRST_CAR_MODEL)
                verify(mockCarRepository, never()).save(any())
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `updates an existing car`() =
            runTest {
                whenever(mockCarRepository.findById(SECOND_CAR_ID)).thenReturn(secondCarEntity)

                carService.update(secondCar)

                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals(
                    "Updated car $SECOND_CAR_YEAR $SECOND_CAR_MAKE $SECOND_CAR_MODEL",
                    appender.list[0].message,
                )

                verify(mockCarRepository, times(1)).findById(SECOND_CAR_ID)
                verify(mockCarRepository, times(1)).save(secondCarEntity)
            }

        @Test
        fun `does not update a car that does not exist`() =
            runTest {
                whenever(mockCarRepository.findById(SECOND_CAR_ID)).thenReturn(null)

                val exception =
                    assertThrows<CarDoesNotExistException> {
                        carService.update(secondCar)
                    }

                assertEquals("$SECOND_CAR_YEAR $SECOND_CAR_MAKE $SECOND_CAR_MODEL does not exist", exception.message)
                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals(
                    "$SECOND_CAR_YEAR $SECOND_CAR_MAKE $SECOND_CAR_MODEL does not exist",
                    appender.list[0].message,
                )

                verify(mockCarRepository, times(1)).findById(SECOND_CAR_ID)
                verify(mockCarRepository, never()).save(any())
            }
    }

    @Nested
    inner class GetAll {
        @Test
        fun `returns all cars`() =
            runTest {
                whenever(mockCarRepository.findAll()).thenReturn(flowOf(firstCarEntity, secondCarEntity))

                val cars = carService.getAll()

                assertEquals(listOf(firstCar, secondCar), cars.toList())
                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Retrieving all cars", appender.list[0].message)
            }
    }

    @Nested
    inner class Delete {
        @Test
        fun `deletes car when car exists`() =
            runTest {
                carService.delete(FIRST_CAR_ID)

                verify(mockCarRepository, times(1)).deleteById(FIRST_CAR_ID)

                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Deleted car with id $FIRST_CAR_ID", appender.list[0].message)
            }
    }

    @BeforeEach
    fun setup() {
        carService = CarService(mockCarRepository)

        logger = LoggerFactory.getLogger(CarService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var carService: CarService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockCarRepository = mock<CarRepository>()

    private val firstCarEntity =
        CarEntity(
            id = FIRST_CAR_ID,
            yearManufactured = FIRST_CAR_YEAR,
            make = FIRST_CAR_MAKE,
            model = FIRST_CAR_MODEL,
        )

    private val firstCar =
        Car(
            id = FIRST_CAR_ID,
            year = FIRST_CAR_YEAR,
            make = FIRST_CAR_MAKE,
            model = FIRST_CAR_MODEL,
        )

    private val secondCarEntity =
        CarEntity(
            id = SECOND_CAR_ID,
            yearManufactured = SECOND_CAR_YEAR,
            make = SECOND_CAR_MAKE,
            model = SECOND_CAR_MODEL,
        )

    private val secondCar =
        Car(
            id = SECOND_CAR_ID,
            year = SECOND_CAR_YEAR,
            make = SECOND_CAR_MAKE,
            model = SECOND_CAR_MODEL,
        )

    companion object CarServiceTestConstants {
        const val FIRST_CAR_ID = 1
        const val FIRST_CAR_YEAR = 2001
        const val FIRST_CAR_MAKE = "Volkswagen"
        const val FIRST_CAR_MODEL = "Jetta"

        const val SECOND_CAR_ID = 2
        const val SECOND_CAR_YEAR = 1999
        const val SECOND_CAR_MAKE = "Chevrolet"
        const val SECOND_CAR_MODEL = "Corvette"
    }
}
