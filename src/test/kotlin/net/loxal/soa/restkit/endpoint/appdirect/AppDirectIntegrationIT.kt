/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.appdirect

import net.loxal.soa.restkit.endpoint.AbstractEndpointTest
import org.junit.Test
import java.net.URI
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

class AppDirectIntegrationIT {
    @Test
    fun createSubscription() {
        val eventId = "dummyOrder"
        val response = AbstractEndpointTest.prepareGenericRequest(SubscriptionResource.RESOURCE_PATH + "/create")
                .queryParam(SubscriptionResource.EVENT_URL_QUERY_PARAM, APP_DIRECT_EVENT_ENDPOINT + eventId)
                .queryParam(SubscriptionResource.TOKEN_QUERY_PARAM, eventId)
                .request()
                .get()

        assertEquals(Response.Status.OK, response.statusInfo)
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.mediaType)
    }

    @Test
    fun changeSubscription() {
        val eventId = "dummyChange"
        val response = AbstractEndpointTest.prepareGenericRequest(SubscriptionResource.RESOURCE_PATH + "/change")
                .queryParam(SubscriptionResource.EVENT_URL_QUERY_PARAM, APP_DIRECT_EVENT_ENDPOINT + eventId)
                .queryParam(SubscriptionResource.TOKEN_QUERY_PARAM, eventId)
                .request()
                .get()

        assertEquals(Response.Status.OK, response.statusInfo)
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.mediaType)
    }

    @Test
    fun cancelSubscription() {
        val eventId = "dummyCancel"
        val response = AbstractEndpointTest.prepareGenericRequest(SubscriptionResource.RESOURCE_PATH + "/cancel")
                .queryParam(SubscriptionResource.EVENT_URL_QUERY_PARAM, APP_DIRECT_EVENT_ENDPOINT + eventId)
                .queryParam(SubscriptionResource.TOKEN_QUERY_PARAM, eventId)
                .request()
                .get()

        assertEquals(Response.Status.OK, response.statusInfo)
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.mediaType)
    }

    @Test
    fun assignUser() {
        val eventId = "dummyAssign"
        val response = AbstractEndpointTest.prepareGenericRequest(AccessResource.RESOURCE_PATH + "/assign")
                .queryParam(SubscriptionResource.EVENT_URL_QUERY_PARAM, APP_DIRECT_EVENT_ENDPOINT + eventId)
                .queryParam(SubscriptionResource.TOKEN_QUERY_PARAM, eventId)
                .request()
                .get()

        assertEquals(Response.Status.OK, response.statusInfo)
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.mediaType)
    }

    @Test
    fun unassignUser() {
        val eventId = "dummyUnassign"
        val response = AbstractEndpointTest.prepareGenericRequest(AccessResource.RESOURCE_PATH + "/unassign")
                .queryParam(SubscriptionResource.EVENT_URL_QUERY_PARAM, APP_DIRECT_EVENT_ENDPOINT + eventId)
                .queryParam(SubscriptionResource.TOKEN_QUERY_PARAM, eventId)
                .request()
                .get()

        assertEquals(Response.Status.OK, response.statusInfo)
        assertEquals(MediaType.APPLICATION_XML_TYPE, response.mediaType)
    }

    @Test
    fun checkDummyChange() {
        val referenceDummyEvent = ChangeEvent(
                type = EventType.SUBSCRIPTION_CHANGE,
                returnUrl = URI.create("https://www.appdirect.com/finishprocure?token=dummyChange"),
                marketplace = EventMarketplace(
                        baseUrl = URI.create("https://acme.appdirect.com"),
                        partner = "ACME"
                ),
                creator = EventCreator(
                        email = "test-email+creator@appdirect.com",
                        firstName = "DummyCreatorFirst",
                        lastName = "DummyCreatorLast",
                        language = "fr",
                        openId = URI.create("https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2"),
                        uuid = "ec5d8eda-5cec-444d-9e30-125b6e4b67e2"
                ),
                payload = ChangeEventPayload(
                        account = EventPayloadAccount(
                                accountIdentifier = "dummy-account",
                                status = "ACTIVE"
                        ),
                        configuration = EventPayloadConfiguration()
                ),
                flag = EventFlag.STATELESS
        )

        val dummyEventResponse = AbstractEndpointTest
                .prepareTarget("${dummyEndpoint}dummyChange")
                .request()
                .get()

        assertEquals(Response.Status.OK.statusCode, dummyEventResponse.status)
        val dummyEvent = dummyEventResponse.readEntity(ChangeEvent::class.java)
        AbstractEndpointTest.LOG.info(dummyEvent.toString())
        assertEquals(referenceDummyEvent, dummyEvent)
    }

    @Test
    fun checkDummyOrder() {
        val referenceDummyOrder = Event(
                type = EventType.SUBSCRIPTION_ORDER,
                returnUrl = URI.create("https://www.appdirect.com/finishprocure?token=dummyOrder"),
                marketplace = EventMarketplace(
                        baseUrl = URI.create("https://acme.appdirect.com"),
                        partner = "ACME"
                ),
                creator = EventCreator(
                        email = "test-email+creator@appdirect.com",
                        firstName = "DummyCreatorFirst",
                        lastName = "DummyCreatorLast",
                        language = "fr",
                        openId = URI.create("https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2"),
                        uuid = "ec5d8eda-5cec-444d-9e30-125b6e4b67e2"
                ),
                payload = EventPayload(
                        company = EventPayloadCompany(
                                country = "CA",
                                email = "company-email@example.com",
                                name = "Example Company Name",
                                phoneNumber = "415-555-1212",
                                uuid = "d15bb36e-5fb5-11e0-8c3c-00262d2cda03",
                                website = URI.create("http://www.example.com")
                        ),
                        configuration = EventPayloadConfiguration(
                                entry = ConfigurationEntry(
                                        key = "domain",
                                        value = "mydomain"
                                )
                        ),
                        order = EventPayloadOrder(
                                editionCode = "BASIC",
                                pricingDuration = "MONTHLY",
                                item = OrderItem(
                                        quantity = 15,
                                        unit = "MEGABYTE"
                                )
                        )
                ),
                flag = EventFlag.STATELESS
        )

        val dummyOrder = AbstractEndpointTest
                .prepareTarget("${dummyEndpoint}dummyOrder")
                .request()
                .get()

        assertEquals(Response.Status.OK.statusCode, dummyOrder.status)
        val dummyOrderEvent = dummyOrder.readEntity(Event::class.java)
        AbstractEndpointTest.LOG.info(dummyOrderEvent.toString())
        assertEquals(referenceDummyOrder, dummyOrderEvent)
    }

    companion object {
        private const val APP_DIRECT_EVENT_ENDPOINT = "https://www.appdirect.com/api/integration/v1/events/"
        private val dummyEndpoint = URI.create("https://www.appdirect.com/rest/api/events/")
    }
}