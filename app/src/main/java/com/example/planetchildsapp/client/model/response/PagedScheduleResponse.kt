package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedScheduleResponse(
    @SerialName("content")
    val content: List<ScheduleResponse>? = null,

    @SerialName("page")
    val page: PageInfo? = null
)
