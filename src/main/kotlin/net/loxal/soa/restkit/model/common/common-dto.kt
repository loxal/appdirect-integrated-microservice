/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.model.common

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.ws.rs.core.Response

/**
 * Detailed error message used in response to provide all errors triggered by a request.
 */
data class ErrorMessage(
        @NotNull val status: Response.Status = Response.Status.BAD_REQUEST
) {
    @NotNull
    val reasonPhrase: String = status.reasonPhrase
    @NotNull
    @Min(value = 100)
    @Max(value = 599)
    var statusCode: Int = status.statusCode
    var message: String? = ""
    var moreInfo: String = ""
    var details: Set<ErrorDetail> = emptySet()
}

data class ErrorDetail private constructor() {
    var field: String = ""
    @NotNull
    var type: String = ""
    var message: String = ""
}

data class Authorization () {
    var access_token: String = ""
    var expires_in: Int = 0
    var token_type: String = ""
    var scope: String = ""
}