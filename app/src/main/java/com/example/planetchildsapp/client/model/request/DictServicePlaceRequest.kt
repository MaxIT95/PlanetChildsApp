package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictServicePlaceRequest(
    @SerialName("name")
    val name: String,
    
    @SerialName("address")
    val address: String? = null,
    
    @SerialName("description")
    val description: String? = null
)
