package com.example.planetchildsapp.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.CoachLinkRequestApi
import com.example.planetchildsapp.client.apis.ScheduleApi
import com.example.planetchildsapp.client.apis.ScheduleRowApi
import com.example.planetchildsapp.client.model.request.ScheduleRowCreateRequest
import com.example.planetchildsapp.client.model.response.ScheduleResponse
import com.example.planetchildsapp.client.model.response.ScheduleRowResponse
import com.example.planetchildsapp.data.repository.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientScheduleItem(
    val id: Long,
    val trainerName: String,
    val date: String,
    val availableFrom: String,
    val availableTo: String,
    val dictServiceId: Long = 0L,
    val preferredFrom: String = "",
    val preferredTo: String = "",
    val assignedFrom: String = "",
    val assignedTo: String = "",
    val scheduleRowId: Long? = null,
    val rowStatus: String? = null,
)

data class ClientScheduleUiState(
    val schedules: List<ClientScheduleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val submitSuccess: Boolean = false,
    val submitError: String? = null,
)

@HiltViewModel
class ClientScheduleViewModel @Inject constructor(
    private val scheduleApi: ScheduleApi,
    private val scheduleRowApi: ScheduleRowApi,
    private val coachLinkRequestApi: CoachLinkRequestApi,
    private val userPrefsRepository: UserPrefsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientScheduleUiState())
    val uiState: StateFlow<ClientScheduleUiState> = _uiState.asStateFlow()

    init {
        loadTrainerAndSchedules()
    }

    fun loadTrainerAndSchedules() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Шаг 1: получаем trainerId через список привязок к тренеру
                val linkResponse = coachLinkRequestApi.getMyLinkRequests(pageNumber = 0, pageSize = 1)
                val trainerId = if (linkResponse.isSuccessful) {
                    linkResponse.body()?.content?.firstOrNull()?.coach?.id
                } else {
                    null
                }

                if (trainerId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Тренер не найден. Сначала привяжите тренера в профиле.",
                        )
                    }
                    return@launch
                }

                // Шаг 2: загружаем расписания тренера
                val schedulesResponse = scheduleApi.getSchedules(
                    pageNumber = 0,
                    trainerId = trainerId.toInt(),
                    pageSize = 20,
                )
                if (!schedulesResponse.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Ошибка загрузки расписания: ${schedulesResponse.code()}",
                        )
                    }
                    return@launch
                }

                val schedules = schedulesResponse.body()?.content.orEmpty()

                // Шаг 3: загружаем существующие заявки текущего пользователя
                val rowsResponse = scheduleRowApi.getScheduleRows(pageNumber = 0, pageSize = 100)
                val existingRows = if (rowsResponse.isSuccessful) {
                    rowsResponse.body()?.content.orEmpty()
                } else {
                    emptyList()
                }

                // Шаг 4: сопоставляем заявки с расписаниями по scheduleId
                val items = schedules.map { schedule ->
                    val existingRow = existingRows.firstOrNull { it.scheduleId == schedule.id }
                    schedule.toClientScheduleItem(existingRow)
                }

                _uiState.update { it.copy(schedules = items, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    fun submitScheduleRow(scheduleId: Long, fromTime: String, toTime: String) {
        viewModelScope.launch {
            val userPrefs = userPrefsRepository.getUserPrefs()
            val clientId = userPrefs.id?.toLongOrNull() ?: run {
                _uiState.update { it.copy(submitError = "Не удалось определить пользователя") }
                return@launch
            }

            val schedule = _uiState.value.schedules.firstOrNull { it.id == scheduleId } ?: run {
                _uiState.update { it.copy(submitError = "Расписание не найдено") }
                return@launch
            }

            if (schedule.scheduleRowId != null) {
                _uiState.update { it.copy(submitError = "Заявка на это расписание уже отправлена") }
                return@launch
            }

            if (schedule.dictServiceId == 0L) {
                _uiState.update { it.copy(submitError = "Тренер не указал услугу для этого расписания") }
                return@launch
            }

            val request = ScheduleRowCreateRequest(
                scheduleId = scheduleId,
                clientId = clientId,
                dictServiceId = schedule.dictServiceId,
                beginTime = fromTime.toApiTime(),
                endTime = toTime.toApiTime(),
            )

            try {
                val response = scheduleRowApi.createScheduleRow(request)
                if (response.isSuccessful) {
                    val row = response.body()
                    _uiState.update { state ->
                        state.copy(
                            submitSuccess = true,
                            submitError = null,
                            schedules = state.schedules.map { item ->
                                if (item.id == scheduleId) {
                                    item.copy(
                                        preferredFrom = fromTime,
                                        preferredTo = toTime,
                                        scheduleRowId = row?.id,
                                        rowStatus = row?.status,
                                    )
                                } else {
                                    item
                                }
                            },
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(submitError = "Ошибка отправки заявки: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(submitError = e.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    fun clearSubmitError() {
        _uiState.update { it.copy(submitError = null) }
    }

    fun clearSubmitSuccess() {
        _uiState.update { it.copy(submitSuccess = false) }
    }
}

private fun String.toApiTime(): String =
    if (count { it == ':' } == 1) "$this:00" else this

private fun ScheduleResponse.toClientScheduleItem(row: ScheduleRowResponse? = null): ClientScheduleItem =
    ClientScheduleItem(
        id = id,
        trainerName = trainerName ?: "Неизвестный тренер",
        date = scheduleDate?.takeIf { it.isNotBlank() } ?: "Дата не указана",
        availableFrom = from?.takeIf { it.isNotBlank() } ?: "00:00",
        availableTo = to?.takeIf { it.isNotBlank() } ?: "00:00",
        dictServiceId = dictServiceId ?: 0L,
        preferredFrom = row?.beginTime?.take(5) ?: "",
        preferredTo = row?.endTime?.take(5) ?: "",
        scheduleRowId = row?.id,
        rowStatus = row?.status,
    )
