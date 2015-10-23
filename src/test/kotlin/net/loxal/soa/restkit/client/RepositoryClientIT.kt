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

        assertEquals(36, token.access_token.length)
        assertEquals(3600, token.expires_in)
        assertEquals("Bearer", token.token_type)
        assertEquals("hybris.document_manage hybris.document_view hybris.tenant=muctool", token.scope)
    }
}