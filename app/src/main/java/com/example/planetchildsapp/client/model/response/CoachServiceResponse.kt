package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoachServiceResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("coach")
    val coach: ProfileResponse,
    
    @SerialName("service")
    val service: DictServiceResponse,
    
    @SerialName("servicePlace")
    val servicePlace: DictServicePlaceResponse,
    
    @SerialName("price")
    val price: Double? = null,
    
    @SerialName("duration")
    val duration: Int? = null,
    
    @SerialName("description")
    val description: String? = null
)
