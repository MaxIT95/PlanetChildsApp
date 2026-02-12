package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictServicePlaceResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("address")
    val address: String? = null,
    
    @SerialName("description")
    val description: String? = null
)
