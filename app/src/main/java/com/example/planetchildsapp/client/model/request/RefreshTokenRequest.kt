package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.Serializable

/**
 * Refresh token request
 */
@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
