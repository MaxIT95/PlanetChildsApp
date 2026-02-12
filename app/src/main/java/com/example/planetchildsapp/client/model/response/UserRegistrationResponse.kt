package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("username")
    val username: String,
    
    @SerialName("email")
    val email: String,
    
    @SerialName("role")
    val role: String
)
