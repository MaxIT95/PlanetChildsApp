package com.example.planetchildsapp.client.model.request

import com.example.planetchildsapp.client.model.enums.ScheduleRowDecision
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleRowStatusUpdateRequest(
    @SerialName("scheduleId")
    val scheduleId: Long,

    @SerialName("status")
    val status: ScheduleRowDecision
)
