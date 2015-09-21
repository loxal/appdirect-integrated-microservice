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
import javax.ws.rs.QueryParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Path(SubscriptionResource.RESOURCE_PATH)
class SubscriptionResource : Endpoint() {

    private var client: RepositoryClient<Any> = RepositoryClient()

    @Path("create")
    @GET
    fun create(
            @QueryParam("url") url: String?,
            @Context requestContext: ContainerRequestContext,
            @Suspended asyncResponse: AsyncResponse) {

        Endpoint.LOG.info("url: $url")

        signUrl()

        val subscription = Subscription(message = Event.SUBSCRIPTION_ORDER.toString())
        println(subscription.success)
        println(subscription.message)
        println(subscription.accountIdentifier)
        println(subscription.errorCode)

        asyncResponse.resume(Response.ok(subscription).build())
    }

    @Path("change")
    @GET
    fun change(@Suspended asyncResponse: AsyncResponse) {
        Endpoint.LOG.info("change get")

        asyncResponse.resume(Response.ok(Subscription(message = "SUBSCRIPTION_CHANGE")).build())
    }

    @Path("cancel")
    @GET
    fun cancel(@Suspended asyncResponse: AsyncResponse) {
        Endpoint.LOG.info("cancel get")

        asyncResponse.resume(Response.ok(Subscription(message = "SUBSCRIPTION_CANCEL")).build())
    }

    @Path("status")
    @GET
    fun status(@Suspended asyncResponse: AsyncResponse) {
        Endpoint.LOG.info("status get")

        asyncResponse.resume(Response.ok(Subscription(message = "SUBSCRIPTION_NOTICE")).build())
    }

    private fun signUrl() {
        val consumer: OAuthConsumer = DefaultOAuthConsumer("Dummy", "secret")
        val url1: URL = URL("https://www.appdirect.com/AppDirect/rest/api/events/dummyChange")
        val request: URLConnection = url1.openConnection()
        val httpRequest: HttpRequest = consumer.sign(request)
        //        println(httpRequest)
        request.connect()


        val consumerSign: OAuthConsumer = DefaultOAuthConsumer("Dummy", "secret")
        consumerSign.setSigningStrategy(QueryStringSigningStrategy())
        val url2: String = "https://www.appdirect.com/AppDirect/finishorder?success=true&accountIdentifer=Alice";
        val signedUrl = consumerSign.sign(url2);
        //        println(signedUrl)
    }

    companion object {
        val APPDIRECT_ROOT_PATH = "appdirect"
        val RESOURCE_PATH = "$APPDIRECT_ROOT_PATH/subscription"
    }
}