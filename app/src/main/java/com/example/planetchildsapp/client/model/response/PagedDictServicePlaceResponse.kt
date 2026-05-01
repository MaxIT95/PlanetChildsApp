package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedDictServicePlaceResponse(
    @SerialName("content")
    val content: List<DictServicePlaceResponse> = emptyList(),

    @SerialName("page")
    val page: PageInfo? = null
)
