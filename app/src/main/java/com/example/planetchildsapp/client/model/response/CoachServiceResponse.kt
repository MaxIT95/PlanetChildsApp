package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoachServiceResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("user")
    val user: SearchUserResponse? = null,

    @SerialName("dictService")
    val dictService: DictServiceResponse? = null,

    @SerialName("costOneTime")
    val costOneTime: Double? = null,

    @SerialName("costMembership")
    val costMembership: Double? = null,

    @SerialName("salary")
    val salary: Double? = null,

    // format: date-time
    @SerialName("createdAt")
    val createdAt: String? = null,

    // format: date-time
    @SerialName("updatedAt")
    val updatedAt: String? = null
)
