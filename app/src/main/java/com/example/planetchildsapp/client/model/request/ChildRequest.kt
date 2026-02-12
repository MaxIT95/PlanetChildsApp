package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildRequest(
    @SerialName("name")
    val name: String,
    
    @SerialName("surname")
    val surname: String,
    
    @SerialName("patronymic")
    val patronymic: String? = null,
    
    @SerialName("birthDate")
    val birthDate: String, // format: date
    
    @SerialName("gender")
    val gender: String,
    
    @SerialName("phoneNumber")
    val phoneNumber: String? = null,
    
    @SerialName("email")
    val email: String? = null
)
