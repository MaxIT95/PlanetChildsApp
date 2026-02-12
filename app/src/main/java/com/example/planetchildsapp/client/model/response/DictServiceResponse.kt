package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictServiceResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("description")
    val description: String? = null
)
