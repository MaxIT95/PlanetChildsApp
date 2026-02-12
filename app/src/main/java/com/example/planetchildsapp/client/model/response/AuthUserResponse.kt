package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserResponse(
    @SerialName("accessToken")
    val accessToken: String,
    
    @SerialName("refreshToken")
    val refreshToken: String,
    
    @SerialName("userId")
    val userId: Long,
    
    @SerialName("username")
    val username: String,
    
    @SerialName("role")
    val role: String
)
