package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedChildResponse(
    @SerialName("content")
    val content: List<ChildResponse>,
    
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)
