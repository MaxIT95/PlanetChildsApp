package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleCreateRequest(
    // Trainer user ID
    @SerialName("trainerId")
    val trainerId: Long,

    // Schedule date, format: date (e.g. "2026-06-15")
    @SerialName("scheduleDate")
    val scheduleDate: String,

    // From what time, format: "HH:mm:ss" (e.g. "13:00:00")
    @SerialName("from")
    val from: String,

    // To what time, format: "HH:mm:ss" (e.g. "14:00:00")
    @SerialName("to")
    val to: String
)
