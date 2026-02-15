package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("email")
    val email: String? = null,
    
    @SerialName("name")
    val name: String? = null,
    
    @SerialName("createdAt")
    val createdAt: String? = null,
    
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    
    @SerialName("profile")
    val profile: ProfileResponse? = null
)
