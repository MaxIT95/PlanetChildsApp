package com.example.planetchildsapp.ui.screen.home.model

data class EventModel(
    val name: String = "",
    val description: String = "",
    // format: date (e.g. "2026-06-15")
    val eventDate: String? = null,
    // Base64-encoded picture
    val picture: String? = null,
)