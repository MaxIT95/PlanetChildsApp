package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoachLinkRequestResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("coach")
    val coach: SearchUserResponse? = null,

    @SerialName("client")
    val client: SearchUserResponse? = null,

    @SerialName("child")
    val child: ChildResponse? = null,

    @SerialName("createdAt")
    val createdAt: String? = null,

    @SerialName("updatedAt")
    val updatedAt: String? = null
)
