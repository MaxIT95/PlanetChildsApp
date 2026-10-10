package com.example.planetchildsapp.ui.screen.home

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.view_model.ClientRequest
import com.example.planetchildsapp.view_model.ClientScheduleItem
import com.example.planetchildsapp.view_model.ClientScheduleViewModel
import com.example.planetchildsapp.view_model.CoachScheduleItem
import com.example.planetchildsapp.view_model.CoachScheduleViewModel
import com.example.planetchildsapp.view_model.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ScheduleScreen(
    _navHost: NavHostController,
    profileViewModel: ProfileViewModel,
    clientScheduleViewModel: ClientScheduleViewModel = hiltViewModel(),
    coachScheduleViewModel: CoachScheduleViewModel = hiltViewModel(),
) {
    val userInfo by profileViewModel.profileState.collectAsState()
    val clientScheduleState by clientScheduleViewModel.uiState.collectAsState()
    val coachScheduleState by coachScheduleViewModel.uiState.collectAsState()
    val isCoach = userInfo.role == "COACH"
    var showAvailabilityDialog by rememberSaveable { mutableStateOf(false) }
    var selectedDate by rememberSaveable { mutableStateOf("") }
    var selectedFromTime by rememberSaveable { mutableStateOf("") }
    var selectedToTime by rememberSaveable { mutableStateOf("") }
    var selectedClientScheduleId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedCoachScheduleId by rememberSaveable { mutableStateOf<Long?>(null) }
    var selectedCoachDate by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedClientSchedule = clientScheduleState.schedules.firstOrNull {
        it.id == selectedClientScheduleId
    }
    val selectedCoachSchedule = coachScheduleState.schedules.firstOrNull {
        it.id == selectedCoachScheduleId
    }

    var submitErrorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(clientScheduleState.submitSuccess) {
        if (clientScheduleState.submitSuccess) {
            selectedClientScheduleId = null
            clientScheduleViewModel.clearSubmitSuccess()
        }
    }

    LaunchedEffect(clientScheduleState.submitError) {
        if (clientScheduleState.submitError != null) {
            submitErrorMessage = clientScheduleState.submitError
            clientScheduleViewModel.clearSubmitError()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AvatarTopBar(userInfo)

        if (isCoach) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { showAvailabilityDialog = true },
                    enabled = !coachScheduleState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Запустить расписание")
                }
            }

            if (coachScheduleState.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (coachScheduleState.error != null) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = coachScheduleState.error ?: "Ошибка загрузки",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                CoachScheduleOverview(
                    modifier = Modifier.weight(1f),
                    schedules = coachScheduleState.schedules,
                    selectedDate = selectedCoachDate,
                    onDateSelected = { selectedCoachDate = it },
                    onScheduleClick = { schedule ->
                        selectedCoachScheduleId = schedule.id
                    }
                )
            }
        } else {
            if (clientScheduleState.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (clientScheduleState.error != null) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = clientScheduleState.error ?: "Ошибка загрузки",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                ClientScheduleCards(
                    modifier = Modifier.weight(1f),
                    schedules = clientScheduleState.schedules,
                    onScheduleClick = { schedule ->
                        selectedClientScheduleId = schedule.id
                    }
                )
            }
        }
    }

    if (showAvailabilityDialog) {
        ScheduleAvailabilityDialog(
            initialDate = selectedDate,
            initialFromTime = selectedFromTime,
            initialToTime = selectedToTime,
            onDismiss = { showAvailabilityDialog = false },
            onConfirm = { date, fromTime, toTime ->
                selectedDate = date
                selectedFromTime = fromTime
                selectedToTime = toTime
                selectedCoachDate = date
                coachScheduleViewModel.launchSchedule(date, fromTime, toTime)
                showAvailabilityDialog = false
            }
        )
    }

    if (selectedCoachSchedule != null) {
        CoachScheduleDetailsDialog(
            schedule = selectedCoachSchedule,
            onDismiss = { selectedCoachScheduleId = null },
            onAssignTime = { clientName, fromTime, toTime ->
                coachScheduleViewModel.assignClientTime(
                    scheduleId = selectedCoachSchedule.id,
                    clientName = clientName,
                    assignedFrom = fromTime,
                    assignedTo = toTime,
                )
            }
        )
    }

    if (selectedClientSchedule != null) {
        ClientScheduleDetailsDialog(
            schedule = selectedClientSchedule,
            onDismiss = { selectedClientScheduleId = null },
            onConfirm = { fromTime, toTime ->
                clientScheduleViewModel.submitScheduleRow(
                    scheduleId = selectedClientSchedule.id,
                    fromTime = fromTime,
                    toTime = toTime,
                )
                // диалог закроется через LaunchedEffect при submitSuccess
            }
        )
    }

    if (submitErrorMessage != null) {
        AlertDialog(
            onDismissRequest = { submitErrorMessage = null },
            title = { Text("Ошибка отправки заявки") },
            text = { Text(submitErrorMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { submitErrorMessage = null }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun CoachScheduleOverview(
    modifier: Modifier = Modifier,
    schedules: List<CoachScheduleItem>,
    selectedDate: String?,
    onDateSelected: (String?) -> Unit,
    onScheduleClick: (CoachScheduleItem) -> Unit,
) {
    val availableDates = schedules.map { it.date }.distinct()
    val filteredSchedules = if (selectedDate == null) {
        schedules
    } else {
        schedules.filter { it.date == selectedDate }
    }

    Column(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Мои запущенные расписания",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Выбери день и посмотри активные окна приёма без таблицы.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                if (selectedDate == null) {
                    Button(onClick = { onDateSelected(null) }) {
                        Text("Все")
                    }
                } else {
                    OutlinedButton(onClick = { onDateSelected(null) }) {
                        Text("Все")
                    }
                }
            }

            items(availableDates) { date ->
                if (selectedDate == date) {
                    Button(onClick = { onDateSelected(date) }) {
                        Text(date)
                    }
                } else {
                    OutlinedButton(onClick = { onDateSelected(date) }) {
                        Text(date)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (filteredSchedules.isEmpty()) {
            CoachScheduleEmptyState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredSchedules,
                    key = { schedule -> schedule.id }
                ) { schedule ->
                    CoachScheduleCard(
                        schedule = schedule,
                        onClick = { onScheduleClick(schedule) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CoachScheduleEmptyState() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "На выбранный день пока нет запущенных расписаний",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Нажми «Запустить расписание», чтобы добавить новое окно приёма.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CoachScheduleCard(
    schedule: CoachScheduleItem,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = schedule.date,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = schedule.status,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Окно приёма: ${schedule.fromTime} — ${schedule.toTime}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Записалось: ${schedule.bookedClients}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Нажми на карточку или «Подробнее», чтобы посмотреть наполнение окна.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onClick) {
                    Text("Подробнее")
                }
            }
        }
    }
}

@Composable
private fun CoachScheduleDetailsDialog(
    schedule: CoachScheduleItem,
    onDismiss: () -> Unit,
    onAssignTime: (String, String, String) -> Unit,
) {
    val context = LocalContext.current
    val timePlaceholder = "Назначить время"
    var assignFromTime by remember { mutableStateOf(timePlaceholder) }
    var assignToTime by remember { mutableStateOf(timePlaceholder) }
    var selectedClientName by remember { mutableStateOf<String?>(null) }
    val fromMinutes = parseTimeToMinutes(assignFromTime)
    val toMinutes = parseTimeToMinutes(assignToTime)
    val hasInvalidAssignTime = fromMinutes != null &&
            toMinutes != null &&
            toMinutes < fromMinutes

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Детали расписания") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Дата: ${schedule.date}")
                Text(text = "Время: ${schedule.fromTime} — ${schedule.toTime}")
                Text(text = "Статус: ${schedule.status}")
                Text(text = "Записалось клиентов: ${schedule.bookedClients}")

                // Таймлайн (временная шкала)
                Text(
                    text = "Таймлайн",
                    style = MaterialTheme.typography.titleSmall
                )
                val timelineBlocks = remember(schedule) {
                    mergeTimelineSlots(
                        generateTimeline(
                            schedule.fromTime,
                            schedule.toTime,
                            schedule.bookedSlots,
                            schedule.clientRequests
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    timelineBlocks.filter { it.status != SlotStatus.FREE }.forEach { block ->
                        val bgColor = when (block.status) {
                            SlotStatus.BOOKED -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            SlotStatus.REQUESTED -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                            SlotStatus.FREE -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        }
                        val textColor = when (block.status) {
                            SlotStatus.BOOKED -> MaterialTheme.colorScheme.primary
                            SlotStatus.REQUESTED -> MaterialTheme.colorScheme.tertiary
                            SlotStatus.FREE -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        val label = buildString {
                            if (block.status == SlotStatus.REQUESTED) {
                                append("${block.clientName}: ${block.startTime}–${block.endTime} (запрос)")
                            } else if (block.status == SlotStatus.BOOKED && block.clientName != null) {
                                append("${block.clientName}: ${block.startTime}–${block.endTime}")
                            } else {
                                append(block.startTime)
                                if (block.clientName != null) {
                                    append("  ${block.clientName}")
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bgColor, RoundedCornerShape(4.dp))
                                .then(
                                    if (block.status == SlotStatus.REQUESTED && block.requestIndex != null) {
                                        Modifier.clickable {
                                            val client = schedule.clientRequests[block.requestIndex!!]
                                            selectedClientName = client.clientName
                                            assignFromTime = timePlaceholder
                                            assignToTime = timePlaceholder
                                        }
                                    } else {
                                        Modifier
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(textColor, CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall,
                                color = textColor
                            )
                        }
                    }
                }

                Text(
                    text = "Записанные клиенты",
                    style = MaterialTheme.typography.titleSmall
                )
                if (schedule.bookedSlots.isEmpty()) {
                    Text(
                        text = "Пока никто не записался.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    schedule.bookedSlots.forEach { bookedSlot ->
                        Text(text = "• $bookedSlot")
                    }
                }

                Text(
                    text = "Запросы клиентов",
                    style = MaterialTheme.typography.titleSmall
                )
                if (schedule.clientRequests.isEmpty()) {
                    Text(
                        text = "Запросов от клиентов пока нет.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    schedule.clientRequests.forEach { request ->
                        val isAssigned = request.assignedFrom.isNotBlank() &&
                                request.assignedTo.isNotBlank()
                        val isSelected = selectedClientName == request.clientName && !isAssigned

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = request.clientName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Интервал: ${request.preferredFrom} — ${request.preferredTo}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (isAssigned) {
                                    Text(
                                        text = "Назначено: ${request.assignedFrom} — ${request.assignedTo}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }

                            if (!isAssigned) {
                                TextButton(
                                    onClick = {
                                        selectedClientName = if (isSelected) null else request.clientName
                                        assignFromTime = timePlaceholder
                                        assignToTime = timePlaceholder
                                    }
                                ) {
                                    Text(if (isSelected) "Отмена" else "Назначить")
                                }
                            }
                        }

                        if (isSelected) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SelectionButton(
                                    label = "С:" ,
                                    value = assignFromTime,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        showTimePicker(context) { selectedTime ->
                                            assignFromTime = selectedTime
                                        }
                                    }
                                )
                                SelectionButton(
                                    label = "До:",
                                    value = assignToTime,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        showTimePicker(context) { selectedTime ->
                                            assignToTime = selectedTime
                                        }
                                    }
                                )
                            }

                            if (hasInvalidAssignTime) {
                                Text(
                                    text = "Время «До» не может быть раньше, чем «С».",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        onAssignTime(
                                            request.clientName,
                                            assignFromTime,
                                            assignToTime
                                        )
                                        selectedClientName = null
                                        assignFromTime = timePlaceholder
                                        assignToTime = timePlaceholder
                                    },
                                    enabled = assignFromTime != timePlaceholder &&
                                            assignToTime != timePlaceholder &&
                                            !hasInvalidAssignTime
                                ) {
                                    Text("Сохранить время")
                                }
                            }
                        }
                    }
                }

            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}

@Composable
private fun ClientScheduleCards(
    modifier: Modifier = Modifier,
    schedules: List<ClientScheduleItem>,
    onScheduleClick: (ClientScheduleItem) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = schedules,
            key = { schedule -> schedule.id }
        ) { schedule ->
            ClientScheduleCard(
                schedule = schedule,
                onClick = { onScheduleClick(schedule) }
            )
        }
    }
}

@Composable
private fun ClientScheduleCard(
    schedule: ClientScheduleItem,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF7F0)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Тренер",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFA06F56)
            )
            Text(
                text = schedule.trainerName,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF6E4332)
            )
            Text(
                text = "${schedule.date} • ${schedule.availableFrom} — ${schedule.availableTo}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6E4332)
            )

            if (schedule.preferredFrom.isNotBlank() && schedule.preferredTo.isNotBlank()) {
                Text(
                    text = "Ваш интервал: ${schedule.preferredFrom} — ${schedule.preferredTo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7C4F3B)
                )
            }

            if (schedule.assignedFrom.isNotBlank() && schedule.assignedTo.isNotBlank()) {
                Text(
                    text = "Назначено: ${schedule.assignedFrom} — ${schedule.assignedTo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF9B5E3C)
                )
            }

            if (schedule.rowStatus != null) {
                val (statusText, statusColor) = when (schedule.rowStatus) {
                    "ACCEPTED" -> "Заявка принята" to Color(0xFF2E7D32)
                    "DECLINED" -> "Заявка отклонена" to MaterialTheme.colorScheme.error
                    else -> "Заявка на рассмотрении" to Color(0xFFF57C00)
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelLarge,
                    color = statusColor,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF6E4332)
                    )
                ) {
                    Text("Подробнее")
                }
            }
        }
    }
}

@Composable
private fun ClientScheduleDetailsDialog(
    schedule: ClientScheduleItem,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit,
) {
    val context = LocalContext.current
    val timePlaceholder = "Выбрать время"
    var fromTime by remember {
        mutableStateOf(schedule.preferredFrom.ifBlank { timePlaceholder })
    }
    var toTime by remember {
        mutableStateOf(schedule.preferredTo.ifBlank { timePlaceholder })
    }
    val availableFromMinutes = parseTimeToMinutes(schedule.availableFrom)
    val availableToMinutes = parseTimeToMinutes(schedule.availableTo)
    val fromTimeMinutes = parseTimeToMinutes(fromTime)
    val toTimeMinutes = parseTimeToMinutes(toTime)
    val hasInvalidTimeRange = fromTimeMinutes != null &&
            toTimeMinutes != null &&
            toTimeMinutes < fromTimeMinutes
    val outsideTrainerWindow = fromTimeMinutes != null &&
            toTimeMinutes != null &&
            availableFromMinutes != null &&
            availableToMinutes != null &&
            (fromTimeMinutes < availableFromMinutes || toTimeMinutes > availableToMinutes)
    val canConfirm = fromTime != timePlaceholder &&
            toTime != timePlaceholder &&
            !hasInvalidTimeRange &&
            !outsideTrainerWindow
    val durationLabel = if (
        canConfirm && fromTimeMinutes != null && toTimeMinutes != null
    ) {
        formatDurationLabel(toTimeMinutes - fromTimeMinutes)
    } else {
        null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(32.dp),
        containerColor = Color(0xFFF8E8DB),
        tonalElevation = 0.dp,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Удобный интервал",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF6E4332)
                )
                Text(
                    text = "Выбери промежуток, когда тебе удобно прийти к тренеру.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFA06F56)
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEFCDB1)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Тренер",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFA06F56)
                        )
                        Text(
                            text = schedule.trainerName,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF6E4332)
                        )
                        Text(
                            text = "${schedule.date} • ${schedule.availableFrom} — ${schedule.availableTo}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF6E4332)
                        )
                        Text(
                            text = "Выбирай только промежуток внутри окна тренера.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA06F56)
                        )
                    }
                }

                if (schedule.scheduleRowId != null) {
                    val statusText = when (schedule.rowStatus) {
                        "ACCEPTED" -> "Принята"
                        "DECLINED" -> "Отклонена"
                        else -> "На рассмотрении"
                    }
                    val statusColor = when (schedule.rowStatus) {
                        "ACCEPTED" -> Color(0xFF2E7D32)
                        "DECLINED" -> MaterialTheme.colorScheme.error
                        else -> Color(0xFFF57C00)
                    }
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3DCC8))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Заявка уже отправлена",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF6E4332)
                            )
                            Text(
                                text = "Ваш интервал: ${schedule.preferredFrom} — ${schedule.preferredTo}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF6E4332)
                            )
                            Text(
                                text = "Статус: $statusText",
                                style = MaterialTheme.typography.bodyLarge,
                                color = statusColor
                            )
                        }
                    }
                }

                if (schedule.scheduleRowId == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ClientTimeSelectionCard(
                            label = "С",
                            value = fromTime,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                showTimePicker(context) { selectedTime ->
                                    fromTime = selectedTime
                                }
                            }
                        )

                        ClientTimeSelectionCard(
                            label = "До",
                            value = toTime,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                showTimePicker(context) { selectedTime ->
                                    toTime = selectedTime
                                }
                            }
                        )
                    }
                }

                if (hasInvalidTimeRange) {
                    Text(
                        text = "Время «До» не может быть раньше, чем «С».",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (outsideTrainerWindow) {
                    Text(
                        text = "Интервал должен быть внутри окна тренера (${schedule.availableFrom} — ${schedule.availableTo}).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (durationLabel != null) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF3DCC8)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Длительность",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFA06F56)
                            )
                            Text(
                                text = durationLabel,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color(0xFF6E4332)
                            )
                            Text(
                                text = "$fromTime — $toTime",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF6E4332)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color(0xFF6E4332).copy(alpha = 0.2f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6E4332)
                    )
                ) {
                    Text("Отмена")
                }
                Button(
                    onClick = { onConfirm(fromTime, toTime) },
                    enabled = canConfirm && schedule.scheduleRowId == null,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6E4332),
                        contentColor = Color(0xFFFFF8F2),
                        disabledContainerColor = Color(0xFF6E4332).copy(alpha = 0.35f),
                        disabledContentColor = Color(0xFFFFF8F2).copy(alpha = 0.7f)
                    )
                ) {
                    Text("Подтвердить")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleAvailabilityDialog(
    initialDate: String,
    initialFromTime: String,
    initialToTime: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
) {
    val context = LocalContext.current
    val datePlaceholder = "Выбрать дату"
    val timePlaceholder = "Выбрать время"
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(initialDate.ifBlank { datePlaceholder }) }
    var fromTime by remember { mutableStateOf(initialFromTime.ifBlank { timePlaceholder }) }
    var toTime by remember { mutableStateOf(initialToTime.ifBlank { timePlaceholder }) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val fromTimeInMinutes = parseTimeToMinutes(fromTime)
    val toTimeInMinutes = parseTimeToMinutes(toTime)
    val hasInvalidTimeRange = fromTimeInMinutes != null &&
            toTimeInMinutes != null &&
            toTimeInMinutes < fromTimeInMinutes
    val canConfirm = selectedDate != datePlaceholder &&
            fromTime != timePlaceholder &&
            toTime != timePlaceholder &&
            !hasInvalidTimeRange

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Доступность тренера") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Выбери дату и время, в которое тренер готов принимать клиентов.")

                SelectionButton(
                    label = "Дата",
                    value = selectedDate,
                    onClick = { showDatePicker = true }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SelectionButton(
                        label = "С:",
                        value = fromTime,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showTimePicker(context) { selectedTime ->
                                fromTime = selectedTime
                            }
                        }
                    )

                    SelectionButton(
                        label = "До:",
                        value = toTime,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showTimePicker(context) { selectedTime ->
                                toTime = selectedTime
                            }
                        }
                    )
                }

                if (hasInvalidTimeRange) {
                    Text(
                        text = "Время «До» не может быть раньше, чем «С».",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedDate, fromTime, toTime) },
                enabled = canConfirm
            ) {
                Text("Готово")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedMillis ->
                        selectedDate = SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.getDefault()
                        ).format(selectedMillis)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun SelectionButton(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(6.dp))
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(value, style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ClientTimeSelectionCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val isSelected = value.contains(":")

    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                Color(0xFF7C4F3B)
            } else {
                Color(0xFF684234)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFFFF2E8).copy(alpha = 0.75f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                color = Color(0xFFFFF8F2)
            )
            Text(
                text = "Нажми, чтобы выбрать",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFFFF2E8).copy(alpha = 0.78f)
            )
        }
    }
}

private fun formatDurationLabel(durationMinutes: Int): String {
    val hours = durationMinutes / 60
    val minutes = durationMinutes % 60

    return when {
        hours > 0 && minutes > 0 -> "$hours ч $minutes мин"
        hours > 0 -> "$hours ч"
        else -> "$minutes мин"
    }
}

private fun parseTimeToMinutes(time: String): Int? {
    val parts = time.split(":")
    if (parts.size != 2) return null

    val hours = parts[0].toIntOrNull() ?: return null
    val minutes = parts[1].toIntOrNull() ?: return null

    return hours * 60 + minutes
}

private fun showTimePicker(
    context: Context,
    onTimeSelected: (String) -> Unit,
) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onTimeSelected(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}

private enum class SlotStatus { BOOKED, REQUESTED, FREE }

private data class TimelineSlot(
    val time: String,
    val status: SlotStatus,
    val clientName: String? = null,
    val requestIndex: Int? = null,
)

private data class TimelineBlock(
    val startTime: String,
    val endTime: String,
    val status: SlotStatus,
    val clientName: String? = null,
    val requestIndex: Int? = null,
)

private fun generateTimeline(
    fromTime: String,
    toTime: String,
    bookedSlots: List<String>,
    clientRequests: List<ClientRequest>,
): List<TimelineSlot> {
    val fromMinutes = parseTimeToMinutes(fromTime) ?: return emptyList()
    val toMinutes = parseTimeToMinutes(toTime) ?: return emptyList()
    if (toMinutes <= fromMinutes) return emptyList()

    // Парсим назначенные интервалы из clientRequests
    val assignedIntervals = clientRequests.mapNotNull { req ->
        val af = parseTimeToMinutes(req.assignedFrom) ?: return@mapNotNull null
        val at = parseTimeToMinutes(req.assignedTo) ?: return@mapNotNull null
        Triple(af, at, req.clientName)
    }

    // Парсим bookedSlots вида "10:00 — 11:00 • София Иванова"
    val bookedIntervals = bookedSlots.mapNotNull { slot ->
        val parts = slot.split("•")
        val timePart = parts.getOrNull(0)?.trim() ?: return@mapNotNull null
        val timeRange = timePart.split("—")
        if (timeRange.size != 2) return@mapNotNull null
        val from = parseTimeToMinutes(timeRange[0].trim()) ?: return@mapNotNull null
        val to = parseTimeToMinutes(timeRange[1].trim()) ?: return@mapNotNull null
        val name = parts.getOrNull(1)?.trim() ?: ""
        Triple(from, to, name)
    }

    // Неопределённые запросы клиентов (без назначения)
    val unassignedRequests = clientRequests.mapIndexedNotNull { index, req ->
        val pf = parseTimeToMinutes(req.preferredFrom) ?: return@mapIndexedNotNull null
        val pt = parseTimeToMinutes(req.preferredTo) ?: return@mapIndexedNotNull null
        if (req.assignedFrom.isNotBlank()) return@mapIndexedNotNull null
        Triple(pf, pt, req.clientName to index)
    }

    val slots = mutableListOf<TimelineSlot>()
    var slotStart = fromMinutes

    while (slotStart < toMinutes) {
        val slotEnd = minOf(slotStart + 30, toMinutes)
        val timeLabel = formatMinutesToTimeLabel(slotStart)

        // Приоритет: BOOKED (назначено или booked) > REQUESTED > FREE
        val bookedHit = (assignedIntervals + bookedIntervals).firstOrNull { (bf, bt, _) ->
            slotStart < bt && slotEnd > bf
        }
        if (bookedHit != null) {
            slots.add(
                TimelineSlot(
                    time = timeLabel,
                    status = SlotStatus.BOOKED,
                    clientName = bookedHit.third.takeIf { it.isNotBlank() }
                )
            )
        } else {
            val requestHit = unassignedRequests.firstOrNull { (pf, pt, _) ->
                slotStart < pt && slotEnd > pf
            }
            if (requestHit != null) {
                slots.add(
                    TimelineSlot(
                        time = timeLabel,
                        status = SlotStatus.REQUESTED,
                        clientName = requestHit.third.first,
                        requestIndex = requestHit.third.second
                    )
                )
            } else {
                slots.add(TimelineSlot(time = timeLabel, status = SlotStatus.FREE))
            }
        }

        slotStart += 30
    }

    return slots
}

private fun mergeTimelineSlots(
    slots: List<TimelineSlot>,
    stepMinutes: Int = 30,
): List<TimelineBlock> {
    val blocks = mutableListOf<TimelineBlock>()
    var i = 0
    while (i < slots.size) {
        val slot = slots[i]
        if ((slot.status == SlotStatus.REQUESTED || slot.status == SlotStatus.BOOKED) && slot.clientName != null) {
            var j = i
            while (j < slots.size &&
                slots[j].status == slot.status &&
                slots[j].clientName == slot.clientName
            ) {
                j++
            }
            val startMinutes = parseTimeToMinutes(slot.time) ?: return emptyList()
            val endMinutes = startMinutes + (j - i) * stepMinutes
            blocks.add(
                TimelineBlock(
                    startTime = slot.time,
                    endTime = formatMinutesToTimeLabel(endMinutes),
                    status = slot.status,
                    clientName = slot.clientName,
                    requestIndex = slot.requestIndex,
                )
            )
            i = j
        } else {
            val startMinutes = parseTimeToMinutes(slot.time) ?: return emptyList()
            blocks.add(
                TimelineBlock(
                    startTime = slot.time,
                    endTime = formatMinutesToTimeLabel(startMinutes + stepMinutes),
                    status = slot.status,
                    clientName = slot.clientName,
                    requestIndex = slot.requestIndex,
                )
            )
            i++
        }
    }
    return blocks
}

private fun formatMinutesToTimeLabel(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return String.format("%02d:%02d", h, m)
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScheduleScreenPreview() {
    val navController = rememberNavController()
    ScheduleScreen(navController, viewModel())
}
