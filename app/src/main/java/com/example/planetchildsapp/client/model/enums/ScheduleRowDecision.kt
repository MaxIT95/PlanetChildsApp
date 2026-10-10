package com.example.planetchildsapp.client.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ScheduleRowDecision {
    @SerialName("ACCEPTED")
    ACCEPTED,

    @SerialName("DECLINED")
    DECLINED
}
