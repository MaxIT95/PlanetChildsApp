package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileContentResponse(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("data")
    val data: String? = null
)
