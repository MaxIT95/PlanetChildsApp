package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRowCreateRequest(
    @SerialName("scheduleId")
    val scheduleId: Long,

    @SerialName("clientId")
    val clientId: Long,

    @SerialName("dictServiceId")
    val dictServiceId: Long,

    @SerialName("beginTime")
    val beginTime: String,

    @SerialName("endTime")
    val endTime: String,

    @SerialName("placeId")
    val placeId: Long? = null,

    @SerialName("visitType")
    val visitType: String? = null
)
