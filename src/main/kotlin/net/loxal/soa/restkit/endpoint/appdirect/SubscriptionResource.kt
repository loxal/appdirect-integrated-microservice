/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.client.AppDirectClient
import net.loxal.soa.restkit.endpoint.Endpoint
import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.DefaultOAuthConsumer
import java.net.URI
import java.net.URL
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Response


// TODO encapsulate all methods in a service layer and delegate to it
@Path(SubscriptionResource.RESOURCE_PATH)
class SubscriptionResource : Endpoint() {
    // TODO improvement proposals for AppDirect
    //  * IPv6 notation for integration URLs (hostname) is not supported
    //  * Integration event list is not shown in user’s local time zone
    //  * AppDirect returned a 5xx on 3 Oct, at 2:30 pm CEST for hours when navigating to the Integration Report page
    @Path("create")
    @GET
    fun create(
            @QueryParam(EVENT_URL_QUERY_PARAM) eventUrl: URL?,
            @QueryParam(TOKEN_QUERY_PARAM) token: String?,
            @Suspended asyncResponse: AsyncResponse
    ) {
        Endpoint.LOG.info("token = $token")

        processAppDirectEvent(asyncResponse, eventUrl, EventType.SUBSCRIPTION_ORDER)
    }

    private fun processAppDirectEvent(asyncResponse: AsyncResponse, eventUrl: URL?, eventType: EventType) {
        val oAuthConsumer: OAuthConsumer = DefaultOAuthConsumer(AppDirectClient.consumerKey, AppDirectClient.consumerSecret)
        val signedUrl = oAuthConsumer.sign(eventUrl.toString())
        val fetchedEvent: Response = appDirectClient.fetchEvent(URI.create(signedUrl))

        Endpoint.LOG.info("eventUrl = $eventUrl")
        Endpoint.LOG.info("signedUrl = $signedUrl")

        if (Response.Status.Family.SUCCESSFUL.equals(fetchedEvent.statusInfo.family)) {
            val created: Event = fetchedEvent.readEntity(Event::class.java);
            val accountIdentifier: String? = created.creator?.uuid

            // TODO additional subscription creation logic

            asyncResponse.resume(Response.ok(Result(
                    success = true,
                    accountIdentifier = accountIdentifier,
                    message = eventType.toString()
            )).build())
        } else {
            asyncResponse.resume(Response.fromResponse(fetchedEvent).build())
        }
    }

    @Path("change")
    @GET
    fun change(
            @QueryParam(EVENT_URL_QUERY_PARAM) eventUrl: URL?,
            @QueryParam(TOKEN_QUERY_PARAM) token: String?,
            @Suspended asyncResponse: AsyncResponse
    ) {
        Endpoint.LOG.info("token = $token")
        // TODO verify OAuth signature

        processAppDirectEvent(asyncResponse, eventUrl, EventType.SUBSCRIPTION_CHANGE)
        //        asyncResponse.resume(Response.ok(Result(
        //                success = true,
        //                message = EventType.SUBSCRIPTION_CHANGE.toString()
        //        )).build())
    }

    @Path("cancel")
    @GET
    fun cancel(
            @QueryParam(EVENT_URL_QUERY_PARAM) eventUrl: URL?,
            @QueryParam(TOKEN_QUERY_PARAM) token: String?,
            @Suspended asyncResponse: AsyncResponse
    ) {
        Endpoint.LOG.info("token = $token")

        // TODO verify OAuth signature

        processAppDirectEvent(asyncResponse, eventUrl, EventType.SUBSCRIPTION_CANCEL)
        //        asyncResponse.resume(Response.ok(Result(
        //                success = true,
        //                message = EventType.SUBSCRIPTION_CANCEL.toString()
        //        )).build())
    }

    @Path("status")
    @GET
    fun status(
            @QueryParam(EVENT_URL_QUERY_PARAM) eventUrl: URL?,
            @QueryParam(TOKEN_QUERY_PARAM) token: String?,
            @Suspended asyncResponse: AsyncResponse
    ) {
        Endpoint.LOG.info("token = $token")

        // TODO verify OAuth signature

        processAppDirectEvent(asyncResponse, eventUrl, EventType.SUBSCRIPTION_NOTICE)
        //        asyncResponse.resume(Response.ok(Result(success = true,
        //                message = EventType.SUBSCRIPTION_NOTICE.toString()
        //        )).build())
    }

    @Path("custom")
    @GET
    fun custom(
            @QueryParam(EVENT_URL_QUERY_PARAM) eventUrl: URL?,
            @QueryParam(TOKEN_QUERY_PARAM) token: String?,
            @Suspended asyncResponse: AsyncResponse
    ) {
        Endpoint.LOG.info("token = $token")

        processAppDirectEvent(asyncResponse, eventUrl, EventType.ADDON)
        //        asyncResponse.resume(Response.ok(Result(
        //                success = true,
        //                message = "ADDON"
        //        )).build())
    }

    companion object {
        const val TOKEN_QUERY_PARAM = "token"
        const val APPDIRECT_ROOT_PATH = "appdirect"
        const val RESOURCE_PATH = "$APPDIRECT_ROOT_PATH/subscription"
        const val EVENT_URL_QUERY_PARAM = "eventUrl"
        private val appDirectClient = AppDirectClient()
    }
}