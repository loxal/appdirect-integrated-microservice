/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.endpoint.ballot

import net.loxal.soa.restkit.endpoint.AbstractEndpointTest
import net.loxal.soa.restkit.model.ballot.Poll
import org.junit.Test
import java.util.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

class PopulateQuestionnaire : AbstractEndpointTest() {
    @Test
    fun populateSimpsonsQuestionnaire() {
        fun populate(polls: List<Poll>) {
            var idx: Int = 0
            polls.forEach { poll ->
                val createdPoll = PollResourceIT.createEntity("simpsons-${++idx}${UUID.randomUUID()}", poll)
                assertEquals(Response.Status.CREATED.statusCode, createdPoll.status)
                assertEquals(MediaType.APPLICATION_JSON_TYPE, createdPoll.mediaType)
                assertEquals(true, createdPoll.location.path.contains("/ballot/poll/"))
                assertEquals(false, createdPoll.location.path.endsWith("/"))

                AbstractEndpointTest.LOG.info("Created poll: ${createdPoll.location}")
            }
        }

        // TODO move this to some manual triggerable “class object” / “object” (and not part of a regular test)
        // TODO populateSimpsonsQuestionnaire with sample data for the iOS app
        // TODO post with ID like simpsons-1 to simpsons-10
        fun createPolls(): List<Poll> {
            val poll1 = Poll("In which town do the Simpsons reside?",
                    listOf(
                            "Springfield-",
                            "Shelbyville",
                            "Seinfeld"
                    )
            )

            val poll2 = Poll("What is the name of the Simpsons' next door neighbor?",
                    listOf(
                            "Barney Gumble",
                            "Ned Flanders-",
                            "Principal Skinner"
                    )
            )

            val poll3 = Poll("Who founded the Simpsons’ town?",
                    listOf(
                            "Jebadiah Springfield-",
                            "Zachariah Springfield",
                            "Springfield Manhattan"
                    )
            )
            val poll4 = Poll("How old is Bart?",
                    listOf(
                            "10-",
                            "11",
                            "12"
                    )
            )
            val poll5 = Poll("What is the name of the clown on Channel 6?",
                    listOf(
                            "Gabbo",
                            "Krusty-",
                            "Bonko"
                    )
            )
            val poll6 = Poll("What is the name of Lisa’s jazz mentor?",
                    listOf(
                            "Billy Jazzman",
                            "Blind Willy Witherspoon",
                            "Bleeding Gums Murphy-"
                    )
            )

            val poll7 = Poll("Who is Mr Burns’ assistant?",
                    listOf(
                            "Seymour Skinner",
                            "Barnard Gumble",
                            "Waylon Smithers-"
                    )
            )
            val poll8 = Poll("What is the name of the bar where Homer drinks?",
                    listOf(
                            "Moe’s Tavern-",
                            "Joe’s Cavern",
                            "The Drink Hole"
                    )
            )
            val poll9 = Poll(" Which one of these is not a catchphrase Bart uses?",
                    listOf(
                            "Aye Carumba!",
                            "Don't have a cow, man!",
                            "Woohoo!-"
                    )
            )
            val poll10 = Poll("What did the Simpsons get for their first Christmas?",
                    listOf(
                            "A dog-",
                            "A cat",
                            "A hamster"
                    )
            )

            return listOf(poll1, poll2, poll3, poll4, poll5, poll6, poll7, poll8, poll9, poll10)
        }

        val polls = createPolls()
        populate(polls)
    }
}