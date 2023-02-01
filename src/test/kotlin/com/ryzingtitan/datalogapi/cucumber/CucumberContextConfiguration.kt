package com.ryzingtitan.datalogapi.cucumber

import com.ryzingtitan.datalogapi.DatalogApiApplication
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@CucumberContextConfiguration
@ActiveProfiles("cucumber")
@SpringBootTest(
    classes = [DatalogApiApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class CucumberContextConfiguration
