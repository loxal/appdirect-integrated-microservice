/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.client

import net.loxal.soa.restkit.endpoint.Endpoint
import net.loxal.soa.restkit.endpoint.appdirect.dto.ADError
import net.loxal.soa.restkit.endpoint.appdirect.dto.Event
import net.loxal.soa.restkit.endpoint.appdirect.dto.EventType
import net.loxal.soa.restkit.endpoint.appdirect.dto.Result
import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.DefaultOAuthConsumer
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URL
import java.util.*
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.core.Response

/**
 * AppDirect client
 */
class ADClient() {

    private fun fetchEvent(uri: URI? = URI.create("")): Response {
        val oAuthConsumer: OAuthConsumer = DefaultOAuthConsumer(consumerKey, consumerSecret)
        val signedUrl = oAuthConsumer.sign(uri.toString())

        val fetchedEvent = CLIENT.target(signedUrl).request().get()

        if (Response.Status.Family.SUCCESSFUL.equals(fetchedEvent.statusInfo.family)) {
            return fetchedEvent
        } else {
            val error = fetchedEvent.readEntity(ADError::class.java)
            Endpoint.LOG.error("error = $error")

            return Response.status(fetchedEvent.status).build()
        }
    }

    fun handleAppDirectEvent(asyncResponse: AsyncResponse, eventUrl: URL?, eventType: EventType) {
        val fetchedEvent: Response = fetchEvent(eventUrl?.toURI())

        if (Response.Status.Family.SUCCESSFUL.equals(fetchedEvent.statusInfo.family)) {
            val created: Event = fetchedEvent.readEntity(Event::class.java);
            val accountIdentifier: String? = created.creator?.uuid

            // TODO additional subscription logic

            asyncResponse.resume(Response.ok(Result(
                    success = true,
                    accountIdentifier = accountIdentifier,
                    message = eventType.toString()
            )).build())
        } else {
            // handle failure in a custom way
            val error = fetchedEvent.readEntity(ADError::class.java)
            Endpoint.LOG.error("error = $error")

            asyncResponse.resume(Response.status(fetchedEvent.status).entity(error).build())
        }
    }


    companion object {
        private val consumerKey: String
        private val consumerSecret: String

        val LOG = LoggerFactory.getLogger(ADClient::class.java)
        private val CLIENT = ClientBuilder.newClient()
        private val properties = Properties()

        init {
            properties.load(ADClient::class.java.getResourceAsStream("/local.properties"))
            consumerKey = properties.getProperty("appdirect.oauth.consumer.key")
            consumerSecret = properties.getProperty("appdirect.oauth.consumer.secret")
        }
    }
}