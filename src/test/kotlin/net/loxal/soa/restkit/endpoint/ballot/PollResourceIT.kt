/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.ballot

import net.loxal.soa.restkit.endpoint.AbstractEndpointTest
import net.loxal.soa.restkit.endpoint.Endpoint
import net.loxal.soa.restkit.model.ballot.Poll
import net.loxal.soa.restkit.model.common.ErrorMessage
import org.junit.Test
import java.util.*
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

class PollResourceIT : AbstractEndpointTest() {
    @Test
    fun createNewPoll() {
        val response = createEntity()

        assertEquals(Response.Status.CREATED.statusCode, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.mediaType)
        assertEquals(false, response.location.path.endsWith("null"))
        assertEquals(true, response.location.path.startsWith(Endpoint.URI_PATH_SEPARATOR + PollResource.RESOURCE_PATH))
        assertEquals(true, response.location.schemeSpecificPart.contains(PollResource.RESOURCE_PATH + Endpoint.URI_PATH_SEPARATOR))
        assertEquals(false, response.location.schemeSpecificPart.endsWith(PollResource.RESOURCE_PATH))
    }

    @Test
    public fun deleteNonExistentPoll() {
        val existingEntity = createEntity()

        val response = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().delete()

        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.mediaType)
        val notFoundStatus = Response.Status.NOT_FOUND
        assertEquals(notFoundStatus.statusCode, response.status)
        val errorMessage = response.readEntity(ErrorMessage::class.java)
        assertEquals(Response.Status.BAD_REQUEST.reasonPhrase, errorMessage.reasonPhrase)
    }

    @Test
    public fun deleteExistingPoll() {
        val existingPoll = createEntity()

        val deletion = AbstractEndpointTest.prepareTarget(existingPoll.location).request().delete()

        assertEquals(Response.Status.NO_CONTENT.statusCode, deletion.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, deletion.mediaType)
    }

    @Test
    public fun retrieveExistingPoll() {
        val existingPoll = createEntity()

        val retrieval = AbstractEndpointTest.prepareTarget(existingPoll.location).request().get()

        assertEquals(Response.Status.OK.statusCode, retrieval.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, retrieval.mediaType)
        val retrievedPoll = retrieval.readEntity<net.loxal.soa.restkit.model.ballot.Poll>(Poll::class.java)
        assertEquals(POLL_QUESTION, retrievedPoll.question)
        assertEquals(POLL_ANSWERS, retrievedPoll.answers)
        assertEquals(2, retrievedPoll.answers.size)
        assertEquals("Yes", retrievedPoll.answers.get(0))
        assertEquals("No", retrievedPoll.answers.get(1))
    }

    @Test
    public fun retrieveNonExistentPoll() {
        val existingEntity = createEntity()

        val retrieval = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().get()


        assertEquals(Response.Status.NOT_FOUND.statusCode, retrieval.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, retrieval.mediaType)
        val notFoundError = retrieval.readEntity(ErrorMessage::class.java)
        assertEquals(Response.Status.BAD_REQUEST.reasonPhrase, notFoundError.reasonPhrase)
    }

    @Test
    public fun updateExistingPoll() {
        val existingPoll = createEntity()

        val newQuestion = "Does it work?"
        val firstAnswerOption = "Maybe"
        val secondAnswerOption = "Somewhat"
        val thirdAnswerOption = "Absolutely Not"
        val newAnswerOptions = Arrays.asList<String>(firstAnswerOption, secondAnswerOption, thirdAnswerOption)

        val modifiedPoll = Poll(newQuestion, newAnswerOptions)

        val update = AbstractEndpointTest.prepareTarget(existingPoll.location).request().put(Entity.json<Poll>(modifiedPoll))

        assertEquals(Response.Status.OK.statusCode, update.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, update.mediaType)

        val retrievedUpdatedPoll = AbstractEndpointTest.prepareTarget(existingPoll.location).request().get()
        val updatedPoll = retrievedUpdatedPoll.readEntity<net.loxal.soa.restkit.model.ballot.Poll>(Poll::class.java)
        assertEquals(newQuestion, updatedPoll.question)
        assertEquals(newAnswerOptions.size.toInt(), updatedPoll.answers.size.toInt())
        assertEquals(firstAnswerOption, updatedPoll.answers.get(0))
        assertEquals(secondAnswerOption, updatedPoll.answers.get(1))
        assertEquals(thirdAnswerOption, updatedPoll.answers.get(2))
    }

    @Test
    public fun updateNonExistentPoll() {
        val existingEntity = createEntity()

        val somePoll = Poll("Irrelevant", listOf<String>())
        val update = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().put(Entity.json<Poll>(somePoll))

        assertEquals(Response.Status.NOT_FOUND.statusCode, update.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, update.mediaType)
    }

    companion object {
        private val POLL_QUESTION = "What is the meaning of life?"
        private val POLL_ANSWERS: List<String> = Arrays.asList<String>("Yes", "No")

        fun createEntity(id: String = UUID.randomUUID().toString(), poll: Poll = Poll(POLL_QUESTION, POLL_ANSWERS)): Response {
            val createdPoll = AbstractEndpointTest.prepareGenericRequest(PollResource.RESOURCE_PATH)
                    .path(id).request()
                    .post(Entity.json<Poll>(poll))
            assertEquals(Response.Status.CREATED.statusCode, createdPoll.status)
            assertEquals(MediaType.APPLICATION_JSON_TYPE, createdPoll.mediaType)

            return createdPoll
        }
    }
}