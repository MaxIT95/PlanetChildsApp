package com.example.planetchildsapp.ui.screen.home.model

data class EventModel(
    val id: Long,
    val name: String = "Мероприятие",
    val description: String = "",
    val eventDate: String? = null,
    val picturePath: String? = null,
)