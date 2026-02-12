package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PagedCoachServiceResponse(
    val content: List<CoachServiceResponse>,
    val pageInfo: PageInfo
)
