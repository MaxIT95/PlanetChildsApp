package com.example.planetchildsapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.ScheduleApi
import com.example.planetchildsapp.client.model.request.ScheduleCreateRequest
import com.example.planetchildsapp.client.model.response.ScheduleResponse
import com.example.planetchildsapp.data.repository.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

data class ClientRequest(
    val clientName: String,
    val preferredFrom: String,
    val preferredTo: String,
    val assignedFrom: String = "",
    val assignedTo: String = "",
)

data class CoachScheduleItem(
    val id: Long,
    val date: String,
    val fromTime: String,
    val toTime: String,
    val status: String,
    val bookedClients: Int,
    val bookedSlots: List<String> = emptyList(),
    val clientRequests: List<ClientRequest> = emptyList(),
)

data class CoachScheduleUiState(
    val schedules: List<CoachScheduleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CoachScheduleViewModel @Inject constructor(
    private val scheduleApi: ScheduleApi,
    private val userPrefsRepository: UserPrefsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoachScheduleUiState())
    val uiState: StateFlow<CoachScheduleUiState> = _uiState.asStateFlow()

    init {
        loadMySchedules()
    }

    fun loadMySchedules() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val prefs = userPrefsRepository.getUserPrefs()
                val trainerId = prefs.id?.toLongOrNull()
                val response = scheduleApi.getSchedules(trainerId = trainerId)
                if (response.isSuccessful) {
                    val items = response.body()?.content.orEmpty().map { it.toCoachScheduleItem() }
                    _uiState.update { it.copy(schedules = items, isLoading = false) }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Ошибка загрузки: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    fun launchSchedule(date: String, fromTime: String, toTime: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val prefs = userPrefsRepository.getUserPrefs()
                val trainerId = prefs.id?.toLongOrNull() ?: run {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Не удалось определить ID тренера")
                    }
                    return@launch
                }
                val request = ScheduleCreateRequest(
                    trainerId = trainerId,
                    scheduleDate = convertDateToIso(date),
                    from = toFullTime(fromTime),
                    to = toFullTime(toTime),
                )
                val response = scheduleApi.createSchedule(request)
                if (response.isSuccessful) {
                    loadMySchedules()
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Ошибка создания расписания: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    /**
     * Назначение времени клиенту — работает только локально.
     * В API отсутствует соответствующий эндпоинт.
     */
    fun assignClientTime(scheduleId: Long, clientName: String, assignedFrom: String, assignedTo: String) {
        _uiState.update { state ->
            state.copy(
                schedules = state.schedules.map { schedule ->
                    if (schedule.id == scheduleId) {
                        schedule.copy(
                            clientRequests = schedule.clientRequests.map { request ->
                                if (request.clientName == clientName) {
                                    request.copy(assignedFrom = assignedFrom, assignedTo = assignedTo)
                                } else {
                                    request
                                }
                            }
                        )
                    } else {
                        schedule
                    }
                }
            )
        }
    }

    // "dd.MM.yyyy" → "yyyy-MM-dd" (формат API)
    private fun convertDateToIso(date: String): String {
        return try {
            val inputFmt = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val outputFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            inputFmt.parse(date)?.let { outputFmt.format(it) } ?: date
        } catch (e: Exception) {
            date
        }
    }

    // "HH:mm" → "HH:mm:ss" (формат API)
    private fun toFullTime(time: String): String =
        if (time.count { it == ':' } == 1) "$time:00" else time
}

private fun ScheduleResponse.toCoachScheduleItem(): CoachScheduleItem {
    return CoachScheduleItem(
        id = id,
        date = scheduleDate ?: "Дата не указана",
        fromTime = from?.takeIf { it.isNotBlank() } ?: "00:00",
        toTime = to?.takeIf { it.isNotBlank() } ?: "00:00",
        status = when (status) {
            "ACTIVE" -> "Активно"
            "EXPIRED" -> "Истекло"
            else -> status ?: "Неизвестно"
        },
        bookedClients = 0,
    )
}
