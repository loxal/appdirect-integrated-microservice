/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.soa.restkit.model.generic

import javax.validation.constraints.NotNull

/**
 * A group of entity references with a name as their description.
 */
class Group(
        @NotNull var name: String = "",
        /**
         * URI list
         */
        @NotNull var entityReferences: List<String> = arrayListOf()
)