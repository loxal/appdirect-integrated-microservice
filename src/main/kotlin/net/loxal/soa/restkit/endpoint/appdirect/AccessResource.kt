/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.client.KitClient
import net.loxal.soa.restkit.endpoint.Endpoint
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Response

@Path(AccessResource.RESOURCE_PATH)
class AccessResource : Endpoint() {

    private var client: KitClient<Any> = KitClient()

    @Path("assign")
    @GET
    fun assign(
            //            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        // TODO verify OAuth signature

        asyncResponse.resume(Response.ok(Result(message = EventType.USER_ASSIGNMENT.toString())).build())
    }

    @Path("unassign")
    @GET
    fun unassign(
            //                 @Context requestContext: ContainerRequestContext,
                 @Suspended asyncResponse: AsyncResponse) {

        // TODO verify OAuth signature

        asyncResponse.resume(Response.ok(Result(message = EventType.USER_UNASSIGNMENT.toString())).build())
    }

    companion object {
        val RESOURCE_PATH = "${SubscriptionResource.APPDIRECT_ROOT_PATH}/user"
    }
}