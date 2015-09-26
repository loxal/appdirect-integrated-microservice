/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.endpoint.Endpoint
import org.openid4java.consumer.ConsumerManager
import org.openid4java.consumer.VerificationResult
import org.openid4java.discovery.DiscoveryInformation
import org.openid4java.message.AuthRequest
import org.openid4java.message.Parameter
import org.openid4java.message.ParameterList
import java.net.URI
import java.net.URL
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Path(OpenIdAuth.RESOURCE_PATH)
class OpenIdAuth : Endpoint() {

    @GET
    fun authenticate(
            @Context req: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse
    ) {
        val responseParameters = ParameterList()
        req.uriInfo.queryParameters.forEach {
            responseParameters.set(Parameter(it.key, it.value.first()))
        }

        var verification: VerificationResult = openIdConsumer.verify(
                req.uriInfo.requestUri.toString(),
                responseParameters,
                endpointAssociation
        )

        if (verification.verifiedId.identifier == null) {
            asyncResponse.resume(Response.status(Response.Status.NOT_ACCEPTABLE).build())
        } else {
            Endpoint.LOG.info(verification.verifiedId.identifier)
            asyncResponse.resume(Response.created(URI.create(verification.verifiedId.identifier)).build())
        }
    }

    @Path("openid")
    @GET
    fun init(
            @QueryParam("url") url: URL,
            @Context req: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse
    ) {
        //        http://localhost:8200/authentication/openid?url=https://www.appdirect.com/openid/id

        val idDiscoveries = openIdConsumer.discover(url.toString())
        endpointAssociation = openIdConsumer.associate(idDiscoveries)

        println(req.uriInfo.baseUri)
        val authReq: AuthRequest = openIdConsumer.authenticate(endpointAssociation, returnToUrl.toString())

        Endpoint.LOG.info("Login: ${authReq.getDestinationUrl(true)}")

        asyncResponse.resume(Response.ok().build())
    }

    companion object {
        val RESOURCE_PATH = "authentication"

        private val returnToUrl: URL = URL("http://localhost:8200/$RESOURCE_PATH")
        private val openIdConsumer: ConsumerManager = ConsumerManager()
        lateinit var endpointAssociation: DiscoveryInformation
    }
}