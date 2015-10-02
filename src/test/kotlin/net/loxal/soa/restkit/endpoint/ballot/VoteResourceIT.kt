/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.ballot

import net.loxal.soa.restkit.endpoint.AbstractEndpointTest
import net.loxal.soa.restkit.endpoint.Endpoint
import net.loxal.soa.restkit.model.ballot.Vote
import net.loxal.soa.restkit.model.common.ErrorMessage
import org.junit.Test
import java.util.*
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

class VoteResourceIT : AbstractEndpointTest() {

    @Test
    fun createVote() {
        val response = createVoteAssignedToPoll()

        assertEquals(Response.Status.CREATED.statusCode, response.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.mediaType)
        assertEquals(false, response.location.path.endsWith("null"))
        assertEquals(true, response.location.path.startsWith(Endpoint.URI_PATH_SEPARATOR + VoteResource.RESOURCE_PATH))
        assertEquals(true, response.location.schemeSpecificPart.contains(VoteResource.RESOURCE_PATH + Endpoint.URI_PATH_SEPARATOR))
        assertEquals(false, response.location.schemeSpecificPart.endsWith(VoteResource.RESOURCE_PATH))
    }

    @Test
    fun deleteNonExistentVote() {
        val existingEntity = createVoteAssignedToPoll()

        val response = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().delete()

        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.mediaType)
        val notFoundStatus = Response.Status.NOT_FOUND
        assertEquals(notFoundStatus.statusCode, response.status)
        val errorMessage = response.readEntity(ErrorMessage::class.java)
        assertEquals(Response.Status.BAD_REQUEST.reasonPhrase, errorMessage.reasonPhrase)
    }

    @Test
    fun deleteExistingVote() {
        val existingVote = createVoteAssignedToPoll()

        val deletion = AbstractEndpointTest.prepareTarget(existingVote.location).request().delete()

        assertEquals(Response.Status.NO_CONTENT.statusCode, deletion.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, deletion.mediaType)
    }

    @Test
    fun retrieveExistingVote() {
        val existingVote = createVoteAssignedToPoll()

        val retrieval = AbstractEndpointTest.prepareTarget(existingVote.location).request().get()

        assertEquals(Response.Status.OK.statusCode, retrieval.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, retrieval.mediaType)

        val retrievedVote = retrieval.readEntity(Vote::class.java)
        assertEquals(ANSWER_OPTION_INDEX.toLong(), retrievedVote.answerOptionIndex.toLong())
        assertEquals(false, retrievedVote.referencePoll.isEmpty())
        assertEquals("anonymous", retrievedVote.user)
    }

    @Test
    fun retrieveExistingNonAnonymousVote() {
        val user = "Specific User"
        val existingVote = createUserVoteAssignedToPoll(user)

        val retrieval = AbstractEndpointTest.prepareTarget(existingVote.location).request().get()

        assertEquals(Response.Status.OK.statusCode, retrieval.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, retrieval.mediaType)

        val retrievedVote = retrieval.readEntity(Vote::class.java)
        assertEquals(ANSWER_OPTION_INDEX.toLong(), retrievedVote.answerOptionIndex.toLong())
        assertEquals(false, retrievedVote.referencePoll.isEmpty())
        assertEquals(user, retrievedVote.user)
    }

    @Test
    fun retrieveNonExistentVote() {
        val existingEntity = createVoteAssignedToPoll()

        val retrieval = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().get()

        assertEquals(Response.Status.NOT_FOUND.statusCode, retrieval.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, retrieval.mediaType)
        val notFoundError = retrieval.readEntity(ErrorMessage::class.java)
        assertEquals(Response.Status.BAD_REQUEST.reasonPhrase, notFoundError.reasonPhrase)
    }

    @Test
    fun updateExistingVote() {
        val existingVote = createVoteAssignedToPoll()

        val updatedReference = "updated reference"
        val updatedAnswerOptionIndex = 2
        val modifiedVote = Vote(updatedReference, updatedAnswerOptionIndex)

        val update = AbstractEndpointTest.prepareTarget(existingVote.location).request().put(Entity.json<Vote>(modifiedVote))

        assertEquals(Response.Status.OK.statusCode, update.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, update.mediaType)

        val retrievedUpdatedPoll = AbstractEndpointTest.prepareTarget(existingVote.location).request().get()
        val updatedVote = retrievedUpdatedPoll.readEntity<net.loxal.soa.restkit.model.ballot.Vote>(Vote::class.java)
        assertEquals(updatedReference, updatedVote.referencePoll)
        assertEquals(updatedAnswerOptionIndex.toLong(), updatedVote.answerOptionIndex.toLong())
    }

    @Test
    fun updateNonExistentVote() {
        val existingEntity = createVoteAssignedToPoll()

        val someVote = Vote("Irrelevant", Integer.MAX_VALUE)
        val update = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().put(Entity.json<Vote>(someVote))

        assertEquals(Response.Status.NOT_FOUND.statusCode, update.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, update.mediaType)
    }

    private fun createVoteAssignedToPoll(): Response {
        val poll = PollResourceIT.createEntity()
        val vote = Vote(poll.location.toString(), ANSWER_OPTION_INDEX)

        val createdVote = AbstractEndpointTest.prepareGenericRequest(VoteResource.RESOURCE_PATH)
                .path(UUID.randomUUID().toString())
                .request().post(Entity.json<Vote>(vote))
        assertEquals(Response.Status.CREATED.statusCode, createdVote.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, createdVote.mediaType)

        return createdVote
    }

    private fun createUserVoteAssignedToPoll(user: String): Response {
        val poll = PollResourceIT.createEntity()
        val vote = Vote.asUser(poll.location.toString(), ANSWER_OPTION_INDEX, user)

        val createdVote = AbstractEndpointTest.prepareGenericRequest(VoteResource.RESOURCE_PATH)
                .path(UUID.randomUUID().toString())
                .request().post(Entity.json<Vote>(vote))
        assertEquals(Response.Status.CREATED.statusCode, createdVote.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, createdVote.mediaType)

        return createdVote
    }

    companion object {
        private const val ANSWER_OPTION_INDEX = 1
    }
}