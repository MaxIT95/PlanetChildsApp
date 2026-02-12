package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    @SerialName("totalElements")
    val totalElements: Long? = null,
    
    @SerialName("totalPages")
    val totalPages: Int? = null,
    
    @SerialName("currentPage")
    val currentPage: Int? = null,
    
    @SerialName("pageSize")
    val pageSize: Int? = null
)
