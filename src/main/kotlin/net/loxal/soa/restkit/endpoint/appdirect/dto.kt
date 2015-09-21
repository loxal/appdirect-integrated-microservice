/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "result")
data class Subscription(
        @XmlElement val success: Boolean = true,
        @XmlElement var accountIdentifier: String = "none",
        @XmlElement var message: String = "SUBSCRIPTION_ORDER",
        @XmlElement var errorCode: ErrorCode = ErrorCode.NONE
)

enum class ErrorCode {
    NONE,
    USER_ALREADY_EXISTS,
    USER_NOT_FOUND,
    ACCOUNT_NOT_FOUND,
    MAX_USERS_REACHED,
    UNAUTHORIZED,
    OPERATION_CANCELED,
    CONFIGURATION_ERROR,
    INVALID_RESPONSE,
    UNKNOWN_ERROR,
    PENDING,
}

enum class Event {
    SUBSCRIPTION_ORDER,
    SUBSCRIPTION_CHANGE,
    SUBSCRIPTION_CANCEL,
    SUBSCRIPTION_NOTICE,

    USER_ASSIGNMENT,
    USER_UNASSIGNMENT,
}