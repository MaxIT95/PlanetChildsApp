package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedScheduleRowResponse(
    @SerialName("content")
    val content: List<ScheduleRowResponse>? = null,

    @SerialName("page")
    val page: PageInfo? = null
)
