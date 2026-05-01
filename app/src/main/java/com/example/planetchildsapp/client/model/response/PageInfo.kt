package com.example.planetchildsapp.client.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    // Number of items per page
    @SerialName("size")
    val size: Int? = null,

    // Current page number (0-indexed)
    @SerialName("number")
    val number: Int? = null,

    @SerialName("totalElements")
    val totalElements: Long? = null,

    @SerialName("totalPages")
    val totalPages: Int? = null,

    // Whether this is the first page
    @SerialName("first")
    val first: Boolean? = null,

    // Whether this is the last page
    @SerialName("last")
    val last: Boolean? = null
)
