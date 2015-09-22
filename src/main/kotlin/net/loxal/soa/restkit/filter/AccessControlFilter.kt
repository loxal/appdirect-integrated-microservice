/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.filter

import javax.annotation.Priority
import javax.ws.rs.Priorities
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.ext.Provider

@Provider
@Priority(Priorities.HEADER_DECORATOR)
class AccessControlFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
        val headers = responseContext.headers

        headers.add(allowOriginHeader, allowOriginHeaderValue)
        headers.put("Server", listOf("RESTkit v1"))

        headers.add(allowHeadersHeader, allowHeadersValue)
    }

    companion object {
        val allowOriginHeader = "Access-Control-Allow-Origin"
        val allowOriginHeaderValue = "*"
        val allowHeadersHeader = "Access-Control-Allow-Headers"
        val allowHeadersValue = listOf("Origin", "X-Requested-With", "")
    }
}
