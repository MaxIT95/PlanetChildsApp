package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PagedDictServicePlaceResponse(
    val content: List<DictServicePlaceResponse>,
    val pageInfo: PageInfo
)
