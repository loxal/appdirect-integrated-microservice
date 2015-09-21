/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.client.RepositoryClient
import net.loxal.soa.restkit.endpoint.Endpoint
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Path(AccessResource.RESOURCE_PATH)
class AccessResource : Endpoint() {

    private var client: RepositoryClient<Any> = RepositoryClient()

    @Path("assign")
    @GET
    fun assign(
            request: Any?,
            @Context requestContext: ContainerRequestContext,
            @PathParam("url") url: String?,
            @PathParam("eventUrl") eventUrl: String?,
            @PathParam("token") token: String?,
            @Suspended asyncResponse: AsyncResponse) {
        println("assign = $request")


        asyncResponse.resume(Response.ok(Subscription(message = Event.USER_ASSIGNMENT.toString())).build())
    }

    @Path("unassign")
    @GET
    fun unassign(request: Any?,
                 @Context requestContext: ContainerRequestContext,
                 @PathParam("url") url: String?,
                 @PathParam("eventUrl") eventUrl: String?,
                 @PathParam("token") token: String?,
                 @Suspended asyncResponse: AsyncResponse) {

        Endpoint.LOG.info("unassign")

        asyncResponse.resume(Response.ok(Subscription(message = Event.USER_UNASSIGNMENT.toString())).build())
    }


    companion object {
        val RESOURCE_PATH = "${SubscriptionResource.APPDIRECT_ROOT_PATH}/user"
    }
}