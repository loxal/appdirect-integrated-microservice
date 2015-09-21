/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.client.RepositoryClient
import net.loxal.soa.restkit.endpoint.Endpoint
import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.DefaultOAuthConsumer
import oauth.signpost.http.HttpRequest
import oauth.signpost.signature.QueryStringSigningStrategy
import java.net.URL
import java.net.URLConnection
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path(SubscriptionResource.RESOURCE_PATH)
class SubscriptionResource : Endpoint() {

    private var client: RepositoryClient<Any> = RepositoryClient()

    @Path("create${Endpoint.URI_PATH_SEPARATOR}$NOTIFICATION_PATH_SUFFIX")
    @GET
    fun create(
            request: Any?,
            @Context requestContext: ContainerRequestContext,
            @PathParam("url") url: String?,
            @PathParam("eventUrl") eventUrl: String?,
            @PathParam("token") token: String?,
            @Suspended asyncResponse: AsyncResponse): Response {

        Endpoint.LOG.info("request = $request")
        Endpoint.LOG.info("request123 = ${requestContext.mediaType}")
        Endpoint.LOG.info("request123 = ${requestContext.propertyNames}")
        requestContext.propertyNames.forEach { i -> Endpoint.LOG.info(i.toString()) }
        Endpoint.LOG.info(url)
        Endpoint.LOG.info(eventUrl)
        Endpoint.LOG.info(token)

        signUrl()

        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_ATOM_XML_TYPE).build()
    }

    private fun signUrl() {
        val consumer: OAuthConsumer = DefaultOAuthConsumer("Dummy", "secret")
        val url1: URL = URL("https://www.appdirect.com/AppDirect/rest/api/events/dummyChange")
        val request: URLConnection = url1.openConnection()
        val httpRequest: HttpRequest = consumer.sign(request)
        println(httpRequest)
        request.connect()


        val consumerSign: OAuthConsumer = DefaultOAuthConsumer("Dummy", "secret")
        consumerSign.setSigningStrategy(QueryStringSigningStrategy())
        val url2: String = "https://www.appdirect.com/AppDirect/finishorder?success=true&accountIdentifer=Alice";
        val signedUrl = consumerSign.sign(url2);
        println(signedUrl)
    }

    @Path("change${Endpoint.URI_PATH_SEPARATOR}$NOTIFICATION_PATH_SUFFIX")
    @GET
    fun change() {
        Endpoint.LOG.info("change get")
    }

    @Path("cancel${Endpoint.URI_PATH_SEPARATOR}$NOTIFICATION_PATH_SUFFIX")
    @GET
    fun cancel() {
        Endpoint.LOG.info("cancel get")
    }

    @Path("status${Endpoint.URI_PATH_SEPARATOR}$NOTIFICATION_PATH_SUFFIX")
    @GET
    fun status() {
        Endpoint.LOG.info("status get")
    }

    companion object {
        val APPDIRECT_ROOT_PATH = "appdirect"
        val RESOURCE_PATH = "$APPDIRECT_ROOT_PATH/subscription"
        val NOTIFICATION_PATH_SUFFIX = "notification"
    }
}