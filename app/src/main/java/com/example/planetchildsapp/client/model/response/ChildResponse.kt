package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String? = null,

    // format: date (e.g. "2018-05-15")
    @SerialName("birthday")
    val birthday: String? = null,

    // Base64-encoded photo
    @SerialName("photo")
    val photo: String? = null,

    @SerialName("parent")
    val parent: SearchUserResponse? = null,

    // format: date-time
    @SerialName("createdAt")
    val createdAt: String? = null,

    // format: date-time
    @SerialName("updatedAt")
    val updatedAt: String? = null
)
