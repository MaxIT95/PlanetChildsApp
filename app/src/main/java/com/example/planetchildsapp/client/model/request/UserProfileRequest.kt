package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.Serializable

/**
 * User profile update request
 */
@Serializable
data class UserProfileRequest(
    val email: String? = null,
    val name: String? = null,
    val description: String? = null,
    val contents: List<ProfileContentRequest>? = null
)