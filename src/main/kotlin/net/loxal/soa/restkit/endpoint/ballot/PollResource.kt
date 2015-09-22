/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.ballot

import net.loxal.soa.restkit.client.RepositoryClient
import net.loxal.soa.restkit.endpoint.Endpoint
import net.loxal.soa.restkit.model.ballot.Poll
import java.util.concurrent.TimeUnit
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.client.Entity
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path(PollResource.RESOURCE_PATH)
class PollResource : Endpoint() {

    private var client: RepositoryClient<Poll> = RepositoryClient()

    @POST
    @Path(Endpoint.ID_PATH_PARAM_PLACEHOLDER)
    fun create(@NotNull @Valid poll: Poll,
               @PathParam(Endpoint.ID_PATH_PARAM) id: String,
               @Context requestContext: ContainerRequestContext,
               @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val createdPoll = client.post(Entity.json<Poll>(poll), id = id)
        val entityLocation = requestContext.uriInfo.requestUri

        asyncResponse.resume(Response.fromResponse(createdPoll).location(entityLocation).build())
        Endpoint.LOG.info(requestContext.method)
    }

    @Path(Endpoint.ID_PATH_PARAM_PLACEHOLDER)
    @DELETE
    fun delete(@NotNull @PathParam(Endpoint.ID_PATH_PARAM) id: String, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val response = client.delete(Poll::class.java, id)

        asyncResponse.resume(Response
                .status(response.status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(response.readEntity(String::class.java))
                .build())
        Endpoint.LOG.info(requestContext.method)
    }

    @Path(Endpoint.ID_PATH_PARAM_PLACEHOLDER)
    @GET
    fun retrieve(@NotNull @PathParam(Endpoint.ID_PATH_PARAM) id: String, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val response = client.get(Poll::class.java, id)

        asyncResponse.resume(Response
                .status(response.status)
                .type(response.mediaType)
                .entity(response.readEntity(String::class.java))
                .build())
        Endpoint.LOG.info(requestContext.method)
    }

    @Path(Endpoint.ID_PATH_PARAM_PLACEHOLDER)
    @PUT
    fun update(@NotNull @Valid poll: Poll, @NotNull @PathParam(Endpoint.ID_PATH_PARAM) id: String, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val updated = client.put(Entity.json<Poll>(poll), id)

        asyncResponse.resume(Response.fromResponse(updated).build())
        Endpoint.LOG.info(requestContext.method)
    }

    companion object {
        private val RESOURCE_NAME = "poll"
        public val RESOURCE_PATH: String = "ballot/" + RESOURCE_NAME
    }
}