package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoachServiceRequest(
    @SerialName("coachId")
    val coachId: Long,
    
    @SerialName("serviceId")
    val serviceId: Long,
    
    @SerialName("servicePlaceId")
    val servicePlaceId: Long,
    
    @SerialName("price")
    val price: Double? = null,
    
    @SerialName("duration")
    val duration: Int? = null,
    
    @SerialName("description")
    val description: String? = null
)
