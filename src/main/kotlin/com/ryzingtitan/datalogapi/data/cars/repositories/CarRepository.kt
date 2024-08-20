package com.ryzingtitan.datalogapi.data.cars.repositories

import com.ryzingtitan.datalogapi.data.cars.entities.CarEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CarRepository : CoroutineCrudRepository<CarEntity, Int> {
    suspend fun findByYearManufacturedAndMakeAndModel(
        yearManufactured: Int,
        make: String,
        model: String,
    ): CarEntity?
}
