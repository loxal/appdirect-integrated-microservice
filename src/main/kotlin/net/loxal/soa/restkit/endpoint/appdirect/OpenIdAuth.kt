/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.endpoint.Endpoint
import javax.ws.rs.Path


@Path(OpenIdAuth.RESOURCE_PATH)
class OpenIdAuth : Endpoint() {

    fun login() {
        //        https://example.com/login?openid_url={openid}


    }

    companion object {
        val RESOURCE_PATH = "/login"
    }

}