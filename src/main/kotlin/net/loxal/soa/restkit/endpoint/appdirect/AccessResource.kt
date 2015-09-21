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
import javax.ws.rs.core.MediaType
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
            @Suspended asyncResponse: AsyncResponse): Response {
        println("assign = $request")


        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_ATOM_XML_TYPE).build()
    }

    @Path("unassign")
    @GET
    fun unassign(request: Any?,
                 @Context requestContext: ContainerRequestContext,
                 @PathParam("url") url: String?,
                 @PathParam("eventUrl") eventUrl: String?,
                 @PathParam("token") token: String?,
                 @Suspended asyncResponse: AsyncResponse): Response {

        Endpoint.LOG.info("unassign")

        return Response.status(Response.Status.CREATED).type(MediaType.APPLICATION_ATOM_XML_TYPE).build()
    }


    companion object {
        val RESOURCE_PATH = "${SubscriptionResource.APPDIRECT_ROOT_PATH}/user"
    }
}