package com.example.planetchildsapp.client.model.enums

import kotlinx.serialization.Serializable

/**
 * User role enumeration
 */
@Serializable
enum class Role {
    PARENT,
    COACH,
    ADMIN
}
