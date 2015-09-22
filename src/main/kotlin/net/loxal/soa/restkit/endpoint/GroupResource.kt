/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint

import net.loxal.soa.restkit.client.RepositoryClient
import net.loxal.soa.restkit.model.generic.Group
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

@Path(GroupResource.RESOURCE_PATH)
class GroupResource : Endpoint() {

    private var client: RepositoryClient<Group> = RepositoryClient()

    @Path(Endpoint.ID_PATH_PARAM_PLACEHOLDER)
    @POST
    fun create(@NotNull @Valid group: Group,
               @PathParam(Endpoint.ID_PATH_PARAM) id: String,
               @Context requestContext: ContainerRequestContext,
               @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val response = client.post(Entity.json<Group>(group), id = id)
        val entityLocation = requestContext.uriInfo.requestUri

        asyncResponse.resume(Response.fromResponse(response).location(entityLocation).build())
        Endpoint.LOG.info(requestContext.method)
    }

    @Path(Endpoint.ID_PATH_PARAM_PLACEHOLDER)
    @DELETE
    fun delete(@NotNull @PathParam(Endpoint.ID_PATH_PARAM) id: String, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val response = client.delete(Group::class.java, id)

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

        val response = client.get(Group::class.java, id)

        asyncResponse.resume(Response
                .status(response.status)
                .type(response.mediaType)
                .entity(response.readEntity(String::class.java))
                .build())
        Endpoint.LOG.info(requestContext.method)
    }

    @Path(Endpoint.ID_PATH_PARAM_PLACEHOLDER)
    @PUT
    fun update(@NotNull @Valid group: Group, @NotNull @PathParam(Endpoint.ID_PATH_PARAM) id: String, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val response = client.put(Entity.json<Group>(group), id)

        asyncResponse.resume(Response.fromResponse(response).build())
        Endpoint.LOG.info(requestContext.method)
    }

    companion object {
        public val RESOURCE_PATH: String = "group"
    }
}