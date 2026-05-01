package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedCoachServiceResponse(
    @SerialName("content")
    val content: List<CoachServiceResponse> = emptyList(),

    @SerialName("page")
    val page: PageInfo? = null
)
