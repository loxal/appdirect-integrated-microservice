/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.MessageFormat
import java.util.regex.Pattern

abstract class Endpoint protected constructor() {
    companion object {
        val URI_PATH_SEPARATOR: String = "/"
        const val ASYNC_RESPONSE_TIMEOUT: Int = 3
        const val ID_PATH_PARAM: String = "id"
        const val ID_PATH_PARAM_PLACEHOLDER: String = "{$ID_PATH_PARAM}"
        val LOG: Logger = LoggerFactory.getLogger(Endpoint::class.java)
        private const val ENTITY_ID_GROUP = "entityIdGroup"
        private val ENTITY_ID_PATTERN = Pattern.compile(MessageFormat.format("/(?<{0}>[0-9a-f-]+)$", ENTITY_ID_GROUP))
    }
}