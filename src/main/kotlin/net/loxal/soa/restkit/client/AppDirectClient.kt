/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.client

import org.slf4j.LoggerFactory
import java.net.URI
import java.util.*
import javax.ws.rs.client.ClientBuilder

class AppDirectClient() {

    fun fetchEvent(uri: URI = URI.create("")) = CLIENT.target(uri).request().get()

    companion object {
        public val consumerKey: String
        public val consumerSecret: String

        val LOG = LoggerFactory.getLogger(AbstractKitClient::class.java)
        private val CLIENT = ClientBuilder.newClient()
        private val properties = Properties()

        init {
            properties.load(KitClient::class.java.getResourceAsStream("/local.properties"))
            consumerKey = properties.getProperty("appdirect.oauth.consumer.key")
            consumerSecret = properties.getProperty("appdirect.oauth.consumer.secret")
        }

    }
}