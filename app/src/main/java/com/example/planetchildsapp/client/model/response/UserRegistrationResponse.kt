package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("email")
    val email: String? = null,

    // Full name (firstName + lastName)
    @SerialName("name")
    val name: String? = null,

    // format: date-time
    @SerialName("createdAt")
    val createdAt: String? = null
)
