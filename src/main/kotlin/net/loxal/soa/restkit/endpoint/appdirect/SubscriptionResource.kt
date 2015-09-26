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

@Path(SubscriptionResource.RESOURCE_PATH)
class SubscriptionResource : Endpoint() {

    private var client: KitClient<Any> = KitClient()

    @Path("create")
    @GET
    fun create(
            //            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        // TODO verify OAuth signature

        asyncResponse.resume(Response.ok(Result(message = EventType.SUBSCRIPTION_ORDER.toString())).build())
    }

    @Path("change")
    @GET
    fun change(
            //            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        // TODO verify OAuth signature

        asyncResponse.resume(Response.ok(Result(message = EventType.SUBSCRIPTION_CHANGE.toString())).build())
    }

    @Path("cancel")
    @GET
    fun cancel(
            //            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        // TODO verify OAuth signature

        asyncResponse.resume(Response.ok(Result(message = EventType.SUBSCRIPTION_CANCEL.toString())).build())
    }

    @Path("status")
    @GET
    fun status(
            //               @Context requestContext: ContainerRequestContext,
               @Suspended asyncResponse: AsyncResponse) {

        // TODO verify OAuth signature

        asyncResponse.resume(Response.ok(Result(message = EventType.SUBSCRIPTION_NOTICE.toString())).build())
    }

    //    private fun showMoreInfo(eventUrl: String?, req: Any?, requestContext: ContainerRequestContext, token: String?, url: String?) {
    //        Endpoint.LOG.info("eventUrl: $eventUrl")
    //        Endpoint.LOG.info("url: $url")
    //        Endpoint.LOG.info("token: $token")
    //        Endpoint.LOG.info("req: $req")
    //
    //        val oAuthHeader: List<String>? = requestContext.headers.get(HttpHeaders.AUTHORIZATION)
    //        requestContext.headers.forEach { header -> println("${header.key}: ${header.value}") }
    //        println(requestContext.uriInfo)
    //        println(requestContext.request)
    //        println(requestContext.uriInfo.baseUri)
    //        println(requestContext.uriInfo.requestUri)
    //
    //        println(requestContext)
    //    }

    @Path("custom")
    @GET
    fun custom(
            //            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse
    ) {

        asyncResponse.resume(Response.ok(Result(message = "ADDON")).build())
    }

    //    private fun signUrl() {
    //        val consumer: OAuthConsumer = DefaultOAuthConsumer(KitClient.consumerKey, KitClient.consumerSecret)
    //    }

    companion object {
        val APPDIRECT_ROOT_PATH = "appdirect"
        val RESOURCE_PATH = "$APPDIRECT_ROOT_PATH/subscription"
    }
}