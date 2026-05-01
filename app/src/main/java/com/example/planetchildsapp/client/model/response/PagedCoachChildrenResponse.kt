package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedCoachChildrenResponse(
    @SerialName("content")
    val content: List<CoachChildrenResponse> = emptyList(),

    @SerialName("page")
    val page: PageInfo? = null
)
