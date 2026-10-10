package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String? = null,

    @SerialName("description")
    val description: String? = null,

    // format: date (e.g. "2026-06-15")
    @SerialName("eventDate")
    val eventDate: String? = null,

    // Base64-encoded picture
    @SerialName("picture")
    val picture: String? = null,

    // format: date-time
    @SerialName("createdAt")
    val createdAt: String? = null,

    // format: date-time
    @SerialName("updatedAt")
    val updatedAt: String? = null
)
