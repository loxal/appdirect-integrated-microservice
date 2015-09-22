/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.client.RepositoryClient
import net.loxal.soa.restkit.endpoint.Endpoint
import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.DefaultOAuthConsumer
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response

@Path(SubscriptionResource.RESOURCE_PATH)
class SubscriptionResource : Endpoint() {

    private var client: RepositoryClient<Any> = RepositoryClient()

    @Path("create")
    @GET
    fun create(
            req: Any?,
            @QueryParam("url") url: String?,
            @QueryParam("eventUrl") eventUrl: String?,
            @QueryParam("token") token: String?,
            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        showMoreInfo(eventUrl, req, requestContext, token, url)


        val r = requestContext.request
        val consumer: OAuthConsumer = DefaultOAuthConsumer(RepositoryClient.consumerKey, RepositoryClient.consumerSecret)
        signUrl()

        asyncResponse.resume(Response.ok(Subscription(message = Event.SUBSCRIPTION_ORDER.toString())).build())
    }

    @Path("change")
    @GET
    fun change(
            req: Any?,
            @QueryParam("url") url: String?,
            @QueryParam("eventUrl") eventUrl: String?,
            @QueryParam("token") token: String?,
            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        showMoreInfo(eventUrl, req, requestContext, token, url)

        asyncResponse.resume(Response.ok(Subscription(message = Event.SUBSCRIPTION_CHANGE.toString())).build())
    }

    @Path("cancel")
    @GET
    fun cancel(
            req: Any?,
            @QueryParam("url") url: String?,
            @QueryParam("eventUrl") eventUrl: String?,
            @QueryParam("token") token: String?,
            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        showMoreInfo(eventUrl, req, requestContext, token, url)

        asyncResponse.resume(Response.ok(Subscription(message = Event.SUBSCRIPTION_CANCEL.toString())).build())
    }

    @Path("status")
    @GET
    fun status(req: Any?,
               @QueryParam("url") url: String?,
               @QueryParam("eventUrl") eventUrl: String?,
               @QueryParam("token") token: String?,
               @Context requestContext: ContainerRequestContext,
               @Suspended asyncResponse: AsyncResponse) {

        showMoreInfo(eventUrl, req, requestContext, token, url)

        asyncResponse.resume(Response.ok(Subscription(message = Event.SUBSCRIPTION_NOTICE.toString())).build())
    }

    private fun showMoreInfo(eventUrl: String?, req: Any?, requestContext: ContainerRequestContext, token: String?, url: String?) {
        Endpoint.LOG.info("eventUrl: $eventUrl")
        Endpoint.LOG.info("url: $url")
        Endpoint.LOG.info("token: $token")
        Endpoint.LOG.info("req: $req")

        val oAuthHeader: List<String>? = requestContext.headers.get(HttpHeaders.AUTHORIZATION)
        requestContext.headers.forEach { header -> println("${header.key}: ${header.value}") }
        println(requestContext.uriInfo)
        println(requestContext.request)
        println(requestContext.uriInfo.baseUri)
        println(requestContext.uriInfo.requestUri)

        println(requestContext)
    }

    @Path("custom")
    @GET
    fun custom(
            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse
    ) {

        asyncResponse.resume(Response.ok(Subscription(message = "ADDON")).build())
    }

    private fun signUrl() {
        val consumer: OAuthConsumer = DefaultOAuthConsumer(RepositoryClient.consumerKey, RepositoryClient.consumerSecret)
        val signed = consumer.sign("https://www.appdirect.com/api/integration/v1/events/adb93bb1-c8ac-4b61-bef2-ff387e09c146")
        println(signed)
    }

    companion object {
        val APPDIRECT_ROOT_PATH = "appdirect"
        val RESOURCE_PATH = "$APPDIRECT_ROOT_PATH/subscription"
    }
}