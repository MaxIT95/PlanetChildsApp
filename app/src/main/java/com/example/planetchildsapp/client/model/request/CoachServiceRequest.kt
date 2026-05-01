package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoachServiceRequest(
    @SerialName("userId")
    val userId: Long,

    @SerialName("dictServiceId")
    val dictServiceId: Long,

    // Cost for one-time service (minimum: 0)
    @SerialName("costOneTime")
    val costOneTime: Double,

    // Cost for membership (minimum: 0)
    @SerialName("costMembership")
    val costMembership: Double,

    // Coach salary for this service (minimum: 0)
    @SerialName("salary")
    val salary: Double
)
