package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedDictServiceResponse(
    @SerialName("content")
    val content: List<DictServiceResponse>,
    
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)
