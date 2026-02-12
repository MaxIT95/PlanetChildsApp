package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoachChildrenResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("child")
    val child: ChildResponse,
    
    @SerialName("coachId")
    val coachId: Long,
    
    @SerialName("linkedAt")
    val linkedAt: String? = null // format: date-time
)
