package com.example.planetchildsapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserStorageInfo(val id: String, val login: String, val password: String)