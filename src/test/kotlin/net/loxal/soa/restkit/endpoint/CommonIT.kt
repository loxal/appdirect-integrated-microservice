/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint

import org.junit.Test
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

class CommonIT {
    @Test
    fun hasIndexWelcomePage() {
        val response = AbstractEndpointTest.prepareGenericRequest(AbstractEndpointTest.resourcePath).request().get()
        AbstractEndpointTest.LOG.info("resourcePath: ${AbstractEndpointTest.resourcePath}")

        assertEquals(Response.Status.OK.statusCode, response.status)
        assertEquals(MediaType.TEXT_HTML_TYPE, response.mediaType)
    }
}