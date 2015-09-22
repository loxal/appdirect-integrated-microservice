/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.service.quote

import net.loxal.soa.restkit.endpoint.AbstractEndpointTest
import net.loxal.soa.restkit.model.whoami.Host
import org.junit.Before
import org.junit.Test
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ResolveIpAddressServiceIT : AbstractEndpointTest() {
    @Before
    public fun setUp() {
        AbstractEndpointTest.resourcePath = ResolveIpAddressService.RESOURCE_PATH
    }

    @Test
    public fun assureHostAddress() {
        val hostName = "localhost"

        val response = AbstractEndpointTest.prepareGenericRequest(ResolveIpAddressService.RESOURCE_PATH).queryParam(ResolveIpAddressService.HOST_NAME_PARAM, hostName).request().get()

        assertEquals(Response.Status.OK.statusCode.toLong(), response.status.toLong())
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.mediaType)

        val host = response.readEntity<Host>(Host::class.java)
        assertNotNull(host.address)
        assertTrue(host.address.length() > 6 && host.address.length() < 20)
        assertEquals(hostName, host.name)
    }
}