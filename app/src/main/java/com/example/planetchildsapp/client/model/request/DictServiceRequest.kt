package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictServiceRequest(
    @SerialName("name")
    val name: String,
    
    @SerialName("description")
    val description: String? = null
)
