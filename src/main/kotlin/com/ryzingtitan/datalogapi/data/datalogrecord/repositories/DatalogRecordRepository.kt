package com.ryzingtitan.datalogapi.data.datalogrecord.repositories

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DatalogRecordRepository : CoroutineCrudRepository<DatalogRecordEntity, Int>
