package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("description")
    val description: String? = null,

    @SerialName("contents")
    val contents: List<ProfileContentResponse> = emptyList()
)
