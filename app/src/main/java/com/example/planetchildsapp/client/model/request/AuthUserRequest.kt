package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.Serializable

/**
 * User authentication request
 */
@Serializable
data class AuthUserRequest(
    val email: String,
    val password: String
)
