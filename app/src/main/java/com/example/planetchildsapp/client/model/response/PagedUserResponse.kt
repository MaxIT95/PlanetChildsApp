package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedUserResponse(
    @SerialName("content")
    val content: List<SearchUserResponse>,
    
    @SerialName("pageInfo")
    val pageInfo: PageInfo
)
