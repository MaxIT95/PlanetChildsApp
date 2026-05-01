package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchUserResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("email")
    val email: String? = null,

    @SerialName("name")
    val name: String? = null
)
