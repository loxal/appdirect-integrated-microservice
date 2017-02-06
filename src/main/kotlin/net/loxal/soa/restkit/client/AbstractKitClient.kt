/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.ws.rs.client.*
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response

abstract class AbstractKitClient<out T> protected constructor() {

    fun targetTenant(webTarget: WebTarget): Invocation.Builder = webTarget.request()

    fun applyToken(request: Invocation.Builder): Invocation.Builder = request.header(HttpHeaders.AUTHORIZATION, "Bearer ${KitClient.authorization.access_token}")

    fun authorizeRequest(target: WebTarget): Invocation.Builder = applyToken(targetTenant(target))

    abstract fun post(entity: Entity<in T>, id: String): Response

    abstract fun delete(entityType: Class<in T>, id: String): Response

    operator abstract fun get(entityType: Class<in T>, id: String): Response

    abstract fun put(json: Entity<in T>, id: String): Response

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(AbstractKitClient::class.java)
        val CLIENT: Client = ClientBuilder.newClient()
    }
}