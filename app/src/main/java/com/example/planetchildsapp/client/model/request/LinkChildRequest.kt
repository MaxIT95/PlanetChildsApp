package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkChildRequest(
    @SerialName("childId")
    val childId: Long,
    
    @SerialName("parentId")
    val parentId: Long
)
