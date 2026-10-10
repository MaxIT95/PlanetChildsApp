package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedCoachLinkRequestResponse(
    @SerialName("content")
    val content: List<CoachLinkRequestResponse> = emptyList(),

    @SerialName("page")
    val page: PageInfo? = null
)
