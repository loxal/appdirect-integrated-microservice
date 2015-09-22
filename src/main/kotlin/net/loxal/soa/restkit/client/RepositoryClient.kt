/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.client

import net.loxal.soa.restkit.model.common.Authorization
import java.net.URI
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Form
import javax.ws.rs.core.MultivaluedHashMap

class RepositoryClient<T> : RestClient<T>() {

    override fun post(entity: Entity<in T>, id: String) =
            authorizeRequest(targetProxy(explicitType(entity)).path(id)).post(entity)

    override fun put(json: Entity<in T>, id: String) =
            authorizeRequest(targetProxy(explicitType(json)).path(id)).put(json)

    override fun delete(entityType: Class<in T>, id: String) =
            authorizeRequest(targetProxy(explicitType(entityType)).path(id)).delete()

    override fun get(entityType: Class<in T>, id: String) =
            authorizeRequest(targetProxy(explicitType(entityType)).path(id)).get()

    private fun targetProxy(entityType: String): WebTarget {
        RestClient.LOG.info("tenant: $tenant | clientId: $clientId | appId: $appId | bearer token: ${authorization.access_token}")
        return RestClient.CLIENT.target(repositoryServiceProxyUrl).path(tenant).path(appId).path(INFIX_PATH).path(entityType)
    }

    private fun explicitType(entity: Entity<in T>) = entity.entity!!.javaClass.simpleName.toLowerCase()

    private fun explicitType(entity: Class<in T>) = entity.simpleName.toLowerCase()

    companion object {
        public val consumerKey: String
        public val consumerSecret: String
        public val appId: String
        public val clientId: String
        val INFIX_PATH: String = "data"
        var authorization = Authorization()
        private val properties = Properties()
        private val tokenRefresher: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
        val repositoryServiceProxyUrl: URI
        val tenant: String

        init {
            properties.load(RepositoryClient::class.java.getResourceAsStream("/local.properties"))
            clientId = properties.getProperty("app.clientId")
            appId = properties.getProperty("appId")
            tenant = properties.getProperty("tenant")
            repositoryServiceProxyUrl = URI.create(properties.getProperty("repositoryServiceProxyUrl"))

            consumerKey = properties.getProperty("appdirect.oauth.consumer.key")
            consumerSecret = properties.getProperty("appdirect.oauth.consumer.secret")

            tokenRefresher.scheduleAtFixedRate(refreshToken(), 0, 3500, TimeUnit.SECONDS)
        }

        private final fun refreshToken() = Runnable {
            val newAuthorization: Authorization
            newAuthorization = authorize()
            authorization = newAuthorization

            RestClient.LOG.info("A new bearer token ${authorization.access_token} has been fetched.")
        }

        final fun authorize(): Authorization {
            val tokenRequestBody = MultivaluedHashMap<String, String>()
            tokenRequestBody.putSingle("grant_type", "client_credentials")
            tokenRequestBody.putSingle("scope", "hybris.document_manage hybris.document_view")
            tokenRequestBody.putSingle("client_id", clientId)
            tokenRequestBody.putSingle("client_secret", properties.getProperty("app.clientSecret"))

            val tokenRequestForm = Form(tokenRequestBody)

            val accessToken = RestClient.CLIENT.target(properties.getProperty("oAuth2ServiceUrl"))
                    .request()
                    .post(Entity.form(tokenRequestForm))

            return accessToken.readEntity(Authorization::class.java)
        }
    }
}