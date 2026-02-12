package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("timestamp")
    val timestamp: String? = null,
    
    @SerialName("status")
    val status: Int? = null,
    
    @SerialName("error")
    val error: String? = null,
    
    @SerialName("message")
    val message: String? = null,
    
    @SerialName("path")
    val path: String? = null
)
