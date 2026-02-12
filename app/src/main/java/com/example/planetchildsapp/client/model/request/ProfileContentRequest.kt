package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.Serializable

/**
 * Profile content request
 */
@Serializable
data class ProfileContentRequest(
    val id: Long? = null,
    val data: String? = null
)
