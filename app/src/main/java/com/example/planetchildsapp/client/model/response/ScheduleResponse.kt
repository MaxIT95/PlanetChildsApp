package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("trainerId")
    val trainerId: Long? = null,

    @SerialName("trainerName")
    val trainerName: String? = null,

    @SerialName("scheduleDate")
    val scheduleDate: String? = null,

    @SerialName("from")
    val from: String? = null,

    @SerialName("to")
    val to: String? = null,

    @SerialName("status")
    val status: String? = null,

    @SerialName("dictServiceId")
    val dictServiceId: Long? = null,
)
