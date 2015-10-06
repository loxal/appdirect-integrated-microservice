/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.client

import net.loxal.soa.restkit.endpoint.AbstractEndpointTest
import org.junit.Test
import kotlin.test.assertEquals

class RepositoryClientIT : AbstractEndpointTest() {

    @Test
    public fun retrieveToken() {
        val token = KitClient.authorize()

        assertEquals(32, token.access_token.length())
        assertEquals(3600, token.expires_in)
        assertEquals("Bearer", token.token_type)
        assertEquals("hybris.tenant=muctool hybris.document_view hybris.document_manage", token.scope)
    }
}