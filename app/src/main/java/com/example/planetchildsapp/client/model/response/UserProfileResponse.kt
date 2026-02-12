package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("userId")
    val userId: Long,
    
    @SerialName("email")
    val email: String? = null,
    
    @SerialName("profileContent")
    val profileContent: ProfileContentResponse? = null
)