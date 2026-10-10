package com.example.planetchildsapp.view_model

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.EventApi
import com.example.planetchildsapp.ui.screen.home.model.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class EventUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val events: List<EventModel> = emptyList(),
)

@HiltViewModel
class EventViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventApi: EventApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response = eventApi.getAllEvents()

                if (response.isSuccessful) {
                    val events = response.body()?.content.orEmpty().map { event ->
                        EventModel(
                            id = event.id,
                            name = event.name?.takeIf { it.isNotBlank() } ?: "Мероприятие",
                            description = event.description?.takeIf { it.isNotBlank() }
                                ?: "Описание отсутствует",
                            eventDate = event.eventDate,
                            picturePath = saveEventPictureLocally(event.id, event.picture)
                        )
                    }

                    _uiState.value = EventUiState(events = events)
                } else {
                    _uiState.value = EventUiState(
                        error = "Не удалось загрузить мероприятия: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("EventViewModel", "Ошибка загрузки мероприятий", e)
                _uiState.value = EventUiState(error = "Ошибка загрузки мероприятий")
            }
        }
    }

    private fun saveEventPictureLocally(eventId: Long, base64Picture: String?): String {
        if (base64Picture.isNullOrBlank()) return ""

        val pictureFile = File(context.filesDir, "event_picture_$eventId.jpg")
        if (pictureFile.exists()) return pictureFile.absolutePath

        return try {
            val normalizedBase64 = base64Picture.substringAfter("base64,", base64Picture)
            val pictureBytes = Base64.decode(normalizedBase64, Base64.DEFAULT)
            pictureFile.writeBytes(pictureBytes)
            pictureFile.absolutePath
        } catch (e: Exception) {
            Log.e("EventViewModel", "Ошибка декодирования картинки мероприятия $eventId", e)
            ""
        }
    }
}