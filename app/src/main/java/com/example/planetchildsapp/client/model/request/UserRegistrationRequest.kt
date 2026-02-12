package com.example.planetchildsapp.client.model.request

import com.example.planetchildsapp.client.model.enums.Role
import kotlinx.serialization.Serializable

/**
 * User registration request
 */
@Serializable
data class UserRegistrationRequest(
    val role: Role,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null
)
