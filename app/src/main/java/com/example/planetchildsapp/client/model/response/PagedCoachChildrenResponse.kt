package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedCoachChildrenResponse(
    @SerialName("content")
    val content: List<CoachChildrenResponse>,
    
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)
