/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint

import net.loxal.soa.restkit.model.common.ErrorMessage
import net.loxal.soa.restkit.model.generic.Group
import org.junit.Test
import java.util.*
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

class GroupResourceIT : AbstractEndpointTest() {

    private fun postEntity(): Response {
        val group = Group("name", ENTRIES)

        // TODO make this entity creation function generic to use it for creation of other entities (reduce code duplication)
        val createdEntity = AbstractEndpointTest.prepareGenericRequest(GroupResource.RESOURCE_PATH)
                .path(UUID.randomUUID().toString())
                .request().post(Entity.json<Group>(group))

        AbstractEndpointTest.LOG.error(createdEntity.readEntity(String::class.java))
        assertEquals(Response.Status.CREATED.statusCode, createdEntity.status)
        assertEquals(MediaType.APPLICATION_JSON_TYPE, createdEntity.mediaType)

        return createdEntity
    }

    @Test
    public fun createEntity() {
        val response = postEntity()

        assertEquals(Response.Status.CREATED.statusCode.toLong(), response.status.toLong())
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.mediaType)
        assertEquals(false, response.location.path.endsWith("null"))
        assertEquals(true, response.location.path.startsWith(Endpoint.URI_PATH_SEPARATOR + GroupResource.RESOURCE_PATH))
        assertEquals(true, response.location.schemeSpecificPart.contains(GroupResource.RESOURCE_PATH + Endpoint.URI_PATH_SEPARATOR))
        assertEquals(false, response.location.schemeSpecificPart.endsWith(GroupResource.RESOURCE_PATH))
    }

    @Test
    public fun deleteNonExistentEntity() {
        val existingEntity = postEntity()

        val deletion = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().delete()

        assertEquals(MediaType.APPLICATION_JSON_TYPE, deletion.mediaType)
        val notFoundStatus = Response.Status.NOT_FOUND
        assertEquals(notFoundStatus.statusCode.toLong(), deletion.status.toLong())
        validateError(deletion)
    }

    @Test
    public fun deleteExistingEntity() {
        val existingEntity = postEntity()

        val deletion = AbstractEndpointTest.prepareTarget(existingEntity.location).request().delete()

        assertEquals(Response.Status.NO_CONTENT.statusCode.toLong(), deletion.status.toLong())
        assertEquals(MediaType.APPLICATION_JSON_TYPE, deletion.mediaType)
    }

    @Test
    public fun retrieveExistingEntity() {
        val existingEntity = postEntity()

        val retrieval = AbstractEndpointTest.prepareTarget(existingEntity.location).request().get()

        assertEquals(Response.Status.OK.statusCode.toLong(), retrieval.status.toLong())
        assertEquals(MediaType.APPLICATION_JSON_TYPE, retrieval.mediaType)
        val retrievedEntity = retrieval.readEntity<Group>(Group::class.java)
        assertEquals(ENTRIES.size.toLong(), retrievedEntity.entityReferences.size.toLong())
        assertEquals(false, retrievedEntity.entityReferences.isEmpty())
    }

    @Test
    public fun retrieveNonExistentEntity() {
        val existingEntity = postEntity()

        val retrieval = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().get()

        assertEquals(Response.Status.NOT_FOUND.statusCode.toLong(), retrieval.status.toLong())
        assertEquals(MediaType.APPLICATION_JSON_TYPE, retrieval.mediaType)
        validateError(retrieval)
    }

    private fun validateError(error: Response) {
        val notFoundError = error.readEntity(ErrorMessage::class.java)
        assertEquals(Response.Status.BAD_REQUEST.reasonPhrase, notFoundError.reasonPhrase)
    }

    @Test
    public fun updateExistingEntity() {
        val existingEntity = postEntity()

        val updatedField = "updated field"
        val entries = Arrays.asList<String>("first", "second")
        val modifiedEntity = Group(updatedField, entries)

        val update = AbstractEndpointTest.prepareTarget(existingEntity.location).request().put(Entity.json<Group>(modifiedEntity))

        assertEquals(Response.Status.OK.statusCode.toLong(), update.status.toLong())
        assertEquals(MediaType.APPLICATION_JSON_TYPE, update.mediaType)

        val retrievedUpdatedEntity = AbstractEndpointTest.prepareTarget(existingEntity.location).request().get()
        val updatedEntity = retrievedUpdatedEntity.readEntity<Group>(Group::class.java)
        assertEquals(updatedField, updatedEntity.name)
        assertEquals(entries.size.toLong(), updatedEntity.entityReferences.size.toLong())
        assertEquals(entries, updatedEntity.entityReferences)
    }

    @Test
    public fun updateNonExistentEntity() {
        val existingEntity = postEntity()

        val someEntity = Group("Irrelevant", listOf<String>())
        val update = AbstractEndpointTest.prepareTarget("${existingEntity.location} ${AbstractEndpointTest.NON_EXISTENT}").request().put(Entity.json<Group>(someEntity))

        assertEquals(Response.Status.NOT_FOUND.statusCode.toLong(), update.status.toLong())
        assertEquals(MediaType.APPLICATION_JSON_TYPE, update.mediaType)
    }

    companion object {
        private val ENTRIES = Arrays.asList<String>("first", "second", "third")
    }
}