/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.service.quote

import net.loxal.soa.restkit.endpoint.Endpoint
import net.loxal.soa.restkit.model.whoami.Host
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Path(WhoamiService.RESOURCE_PATH)
class WhoamiService : Endpoint() {

    @GET
    fun whoAmI(@Context request: HttpServletRequest, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val host = Host(request.remoteHost, request.remoteAddr)
        asyncResponse.resume(Response.ok(host).build())

        LOG.info(requestContext.method)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(WhoamiService::class.java)
        val RESOURCE_PATH = "who-am-i"
    }
}