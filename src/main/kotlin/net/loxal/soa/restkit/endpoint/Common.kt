/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint

import java.net.URI
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Response

@Path("/")
class Common {
    @GET
    fun index(@Suspended asyncResponse: AsyncResponse) {
        asyncResponse.resume(Response.temporaryRedirect(URI.create("index.html")).build())
    }
}