package com.ryzingtitan.datalogapi.cucumber.repositories

import com.ryzingtitan.datalogapi.data.datalog.entities.DataEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.DatalogEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.TrackInfoEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.UserEntity
import com.ryzingtitan.datalogapi.data.datalog.repositories.DatalogRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.UUID

class DatalogRepositoryStepDefs(
    private val datalogRepository: DatalogRepository,
) {

    @Given("the following datalogs exist:")
    fun givenTheFollowingDatalogsExist(table: DataTable) {
        val datalogEntities =
            table.tableConverter.toList<DatalogEntity>(table, DatalogEntity::class.java)

        datalogEntities.forEach { datalog ->
            runBlocking {
                datalogRepository.save(datalog)
            }
        }
    }

    @Then("the following datalogs will exist:")
    fun thenTheFollowingDatalogsWillExist(table: DataTable) {
        val expectedDatalogs =
            table.tableConverter.toList<DatalogEntity>(table, DatalogEntity::class.java)
                .sortedBy { it.epochMilliseconds }

        val actualDatalogs = mutableListOf<DatalogEntity>()
        runBlocking {
            datalogRepository.findAll().collect { datalog ->
                actualDatalogs.add(datalog)
            }
        }

        assertEquals(
            expectedDatalogs.sortedBy { it.epochMilliseconds },
            actualDatalogs.sortedBy { it.epochMilliseconds },
        )
    }

    @Before
    fun setup() {
        runBlocking {
            datalogRepository.deleteAll()
        }
    }

    @DataTableType
    fun mapDatalogEntity(tableRow: Map<String, String>): DatalogEntity {
        return DatalogEntity(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            epochMilliseconds = tableRow["epochMilliseconds"].toString().toLong(),
            data = DataEntity(
                longitude = tableRow["longitude"].toString().toDouble(),
                latitude = tableRow["latitude"].toString().toDouble(),
                altitude = tableRow["altitude"].toString().toFloat(),
                intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toIntOrNull(),
                boostPressure = tableRow["boostPressure"].toString().toFloatOrNull(),
                coolantTemperature = tableRow["coolantTemperature"].toString().toIntOrNull(),
                engineRpm = tableRow["engineRpm"].toString().toIntOrNull(),
                speed = tableRow["speed"].toString().toIntOrNull(),
                throttlePosition = tableRow["throttlePosition"].toString().toFloatOrNull(),
                airFuelRatio = tableRow["airFuelRatio"].toString().toFloatOrNull(),
            ),
            trackInfo = TrackInfoEntity(
                name = tableRow["trackName"].toString(),
                latitude = tableRow["trackLatitude"].toString().toDouble(),
                longitude = tableRow["trackLongitude"].toString().toDouble(),
            ),
            user = UserEntity(
                firstName = tableRow["firstName"].toString(),
                lastName = tableRow["lastName"].toString(),
                email = tableRow["email"].toString(),
            ),
        )
    }
}
