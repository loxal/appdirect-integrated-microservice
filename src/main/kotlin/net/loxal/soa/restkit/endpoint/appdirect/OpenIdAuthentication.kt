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
import javax.validation.constraints.NotNull
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Path(OpenIdAuthentication.RESOURCE_PATH)
class OpenIdAuthentication : Endpoint() {

    @GET
    fun verify(
            @Context req: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse
    ) {
        val responseParameters = ParameterList()
        req.uriInfo.queryParameters.forEach {
            responseParameters.set(Parameter(it.key, it.value.first()))
        }

        val verification: VerificationResult = openIdConsumer.verify(
                req.uriInfo.requestUri.toString(),
                responseParameters,
                endpointAssociation
        )

        if (verification.verifiedId.identifier == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).build())
        } else {
            asyncResponse.resume(Response.ok(URI.create(verification.verifiedId.identifier)).build())
        }
    }

    @Path("openid")
    @GET
    fun authenticate(
            @QueryParam("url") url: URL,
            @NotNull @QueryParam("returnToUrl") returnToUrl: URL,
            @Suspended asyncResponse: AsyncResponse
    ) {
        val idDiscoveries = openIdConsumer.discover(url.toString())
        endpointAssociation = openIdConsumer.associate(idDiscoveries)

        val authReq: AuthRequest = openIdConsumer.authenticate(endpointAssociation, returnToUrl.toString())

        val signInUrl: URL = URL(authReq.getDestinationUrl(true))
        val openIdInfo = OpenIdInfo(url = url, returnToUrl = returnToUrl, signInUrl = signInUrl)

        asyncResponse.resume(Response.ok(openIdInfo).location(signInUrl.toURI()).build())
    }

    companion object {
        val RESOURCE_PATH = "authentication"

        private val openIdConsumer: ConsumerManager = ConsumerManager()
        lateinit var endpointAssociation: DiscoveryInformation
    }
}