package com.ryzingtitan.datalogapi.domain.session.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.GONE)
class SessionDoesNotExistException(message: String) : Exception(message)
