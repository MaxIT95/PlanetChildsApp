package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileContentResponse(
    @SerialName("id")
    val id: Long,
    
    @SerialName("name")
    val name: String? = null,
    
    @SerialName("surname")
    val surname: String? = null,
    
    @SerialName("patronymic")
    val patronymic: String? = null,
    
    @SerialName("birthDate")
    val birthDate: String? = null, // format: date
    
    @SerialName("gender")
    val gender: String? = null,
    
    @SerialName("phoneNumber")
    val phoneNumber: String? = null
)
