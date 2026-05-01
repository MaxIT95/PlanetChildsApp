package com.example.planetchildsapp.client.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildRequest(
    @SerialName("name")
    val name: String,

    @SerialName("birthday")
    val birthday: String
)
