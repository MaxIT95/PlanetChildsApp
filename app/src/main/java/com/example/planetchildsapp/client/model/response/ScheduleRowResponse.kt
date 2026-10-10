package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRowResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("scheduleId")
    val scheduleId: Long? = null,

    @SerialName("trainerId")
    val trainerId: Long? = null,

    @SerialName("trainerName")
    val trainerName: String? = null,

    @SerialName("coachClientsId")
    val coachClientsId: Long? = null,

    @SerialName("clientId")
    val clientId: Long? = null,

    @SerialName("clientName")
    val clientName: String? = null,

    @SerialName("dictServiceId")
    val dictServiceId: Long? = null,

    @SerialName("placeId")
    val placeId: Long? = null,

    @SerialName("beginTime")
    val beginTime: String? = null,

    @SerialName("endTime")
    val endTime: String? = null,

    @SerialName("visitType")
    val visitType: String? = null,

    @SerialName("status")
    val status: String? = null,

    // format: date-time
    @SerialName("createdAt")
    val createdAt: String? = null
)
