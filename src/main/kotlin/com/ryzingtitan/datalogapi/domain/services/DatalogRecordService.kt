package com.ryzingtitan.datalogapi.domain.services

import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import org.springframework.stereotype.Service

@Service
class DatalogRecordService(private val datalogRecordRepository: DatalogRecordRepository)
