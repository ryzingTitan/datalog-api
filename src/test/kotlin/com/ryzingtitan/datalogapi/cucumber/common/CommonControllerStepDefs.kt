package com.ryzingtitan.datalogapi.cucumber.common

import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.http.HttpStatus

class CommonControllerStepDefs {
    @Then("the request response status is {string}")
    fun theRequestResponseStatusIs(statusCode: String) {
        assertEquals(statusCode, responseStatus?.name)
    }

    companion object CommonControllerStepDefsSharedState {
        internal var responseStatus: HttpStatus? = null
    }
}
