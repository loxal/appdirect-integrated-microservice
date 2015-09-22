/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.service.quote

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import net.loxal.soa.restkit.endpoint.Endpoint
import net.loxal.soa.restkit.model.dilbert.Quote
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Produces(DilbertQuoteService.mediaType)
@Path(DilbertQuoteService.RESOURCE_PATH)
class DilbertQuoteService : Endpoint() {

    @Path(RESOURCE_PATH_ENTERPRISE)
    @GET
    fun quoteEnterprise(@Context request: HttpServletRequest, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        fetchRandomQuote(asyncResponse, request, requestContext, quotes = quotesEnterprise)
    }

    private fun fetchRandomQuote(asyncResponse: AsyncResponse, request: HttpServletRequest, requestContext: ContainerRequestContext, quotes: List<Quote>) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val randomQuoteIndex = random.nextInt(quotes.size())
        asyncResponse.resume(Response.ok(quotes[randomQuoteIndex]).build())

        LOG.info("${requestContext.method} for $randomQuoteIndex ${request.contextPath}")
    }

    @Path(RESOURCE_PATH_MANAGER)
    @GET
    fun quoteManager(@Context request: HttpServletRequest, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        fetchRandomQuote(asyncResponse, request, requestContext, quotes = quotesManager)
    }

    @Path(RESOURCE_PATH_PROGRAMMER)
    @GET
    fun quoteProgrammer(@Context request: HttpServletRequest, @Context requestContext: ContainerRequestContext, @Suspended asyncResponse: AsyncResponse) {
        fetchRandomQuote(asyncResponse, request, requestContext, quotes = quotesProgrammer)
    }

    @Path("$RESOURCE_PATH_ENTERPRISE/${Endpoint.ID_PATH_PARAM_PLACEHOLDER}")
    @GET
    fun quoteEnterprise(@Context request: HttpServletRequest, @Context requestContext: ContainerRequestContext,
                        @PathParam(Endpoint.ID_PATH_PARAM) quoteId: Int,
                        @Suspended asyncResponse: AsyncResponse) {
        fetchQuoteById(asyncResponse, quoteId, request, requestContext, quotes = quotesEnterprise)
    }

    private fun fetchQuoteById(asyncResponse: AsyncResponse, quoteId: Int, request: HttpServletRequest, requestContext: ContainerRequestContext, quotes: List<Quote>) {
        asyncResponse.setTimeout(Endpoint.ASYNC_RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val quoteIndex = quoteId - 1
        asyncResponse.resume(Response.ok(quotes[quoteIndex]).build())

        LOG.info("${requestContext.method} for $quoteId ${request.contextPath}")
    }

    @Path("$RESOURCE_PATH_MANAGER/${Endpoint.ID_PATH_PARAM_PLACEHOLDER}")
    @GET
    fun quoteManager(@Context request: HttpServletRequest, @Context requestContext: ContainerRequestContext,
                     @PathParam(Endpoint.ID_PATH_PARAM) quoteId: Int,
                     @Suspended asyncResponse: AsyncResponse) {
        fetchQuoteById(asyncResponse, quoteId, request, requestContext, quotes = quotesManager)
    }

    @Path("$RESOURCE_PATH_PROGRAMMER/${Endpoint.ID_PATH_PARAM_PLACEHOLDER}")
    @GET
    fun quoteProgrammer(@Context request: HttpServletRequest, @Context requestContext: ContainerRequestContext,
                        @PathParam(Endpoint.ID_PATH_PARAM) quoteId: Int,
                        @Suspended asyncResponse: AsyncResponse) {
        fetchQuoteById(asyncResponse, quoteId, request, requestContext, quotes = quotesProgrammer)
    }

    init {
        quotesEnterpriseReader.close()
        quotesManagerReader.close()
        quotesProgrammerReader.close()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DilbertQuoteService::class.java)

        private data class Quotes : TypeReference<List<Quote>>()

        private val quotesType = Quotes()
        private val objectMapper = ObjectMapper()

        private val quotesEnterpriseReader = InputStreamReader(DilbertQuoteService::class.java.getResourceAsStream("quotes-dilbert-enterprise.json"))
        private val quotesManagerReader = InputStreamReader(DilbertQuoteService::class.java.getResourceAsStream("quotes-dilbert-manager.json"))
        private val quotesProgrammerReader = InputStreamReader(DilbertQuoteService::class.java.getResourceAsStream("quotes-dilbert-programmer.json"))

        private val quoteEnterpriseData = quotesEnterpriseReader.readText()
        private val quoteManagerData = quotesManagerReader.readText()
        private val quoteProgrammerData = quotesProgrammerReader.readText()
        val quotesEnterprise: List<Quote> = objectMapper.readValue(quoteEnterpriseData, quotesType)
        val quotesManager: List<Quote> = objectMapper.readValue(quoteManagerData, quotesType)
        val quotesProgrammer: List<Quote> = objectMapper.readValue(quoteProgrammerData, quotesType)
        private val random: Random = Random()

        val RESOURCE_PATH = "dilbert-quote"
        val RESOURCE_PATH_PROGRAMMER = "programmer"
        val RESOURCE_PATH_MANAGER = "manager"
        val RESOURCE_PATH_ENTERPRISE = "enterprise"
        val mediaType = MediaType.APPLICATION_JSON + ";charset=utf-8"
    }
}