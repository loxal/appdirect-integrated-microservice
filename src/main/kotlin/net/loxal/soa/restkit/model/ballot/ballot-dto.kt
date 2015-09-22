/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.model.ballot

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class Poll(@NotNull var question: String = "", @NotNull var answers: List<String> = arrayListOf())

data class Vote(
        @NotNull var referencePoll: String = "",
        @NotNull @Min(value = 0) var answerOptionIndex: Int = 0
) {
    var user: String = "anonymous"

    companion object {
        fun asUser(referencePoll: String, answerOptionIndex: Int, user: String): Vote {
            val vote = Vote(referencePoll = referencePoll, answerOptionIndex = answerOptionIndex)
            vote.user = user

            return vote
        }
    }
}