package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateLinkRequestRequest(
    @SerialName("coachId")
    val coachId: Long? = null,

    @SerialName("clientId")
    val clientId: Long? = null,

    @SerialName("childId")
    val childId: Long? = null
)
