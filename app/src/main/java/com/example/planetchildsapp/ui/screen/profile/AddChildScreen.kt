package com.example.planetchildsapp.ui.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.ui.screen.common.BackStackElement
import com.example.planetchildsapp.ui.screen.enter.EnterTextField
import com.example.planetchildsapp.ui.screen.home.Avatar
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.view_model.ChildViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddChildScreen(
    navHost: NavHostController,
    childViewModel: ChildViewModel
) {
    // очищаем состояние один раз при входе на экран,
    // иначе каждая рекомпозиция (в т.ч. после выбора аватара) будет затирать state
    LaunchedEffect(Unit) {
        childViewModel.clearState()
    }

    Column(modifier = Modifier.padding(5.dp)) {
        BackStackElement(navHost)
        ChildAddCard(
            childViewModel = childViewModel,
            onAvatarSelected = { childViewModel.onAvatarSelected(it) }
        )
    }
}

@Composable
fun ChildAddCard(
    childViewModel: ChildViewModel,
    onAvatarSelected: (Uri) -> Unit
) {

    val childState by childViewModel.childState.collectAsState()

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, top = 10.dp, end = 30.dp)
        ) {
            Avatar(
                avatarPath = childState.avatarPath,
                onAvatarSelected = onAvatarSelected,
                imageSize = 180.dp
            )
            Spacer(Modifier.padding(vertical = 5.dp))

            // Имя
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Имя",
                textAlign = TextAlign.Start,
                style = PlanetChildAppTypography.titleSmall
            )

            EnterTextField(
                childState.name,
                { childViewModel.onNameChange(it) }, childState.isNameValid,
                "Введите имя ребенка", Icons.Default.Person,
                horizontalPadding = 0.dp
            )

            Spacer(Modifier.padding(vertical = 10.dp))

            // Дата рождения
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Укажите дату рождения",
                textAlign = TextAlign.Start,
                style = PlanetChildAppTypography.titleSmall
            )

            BirthDayPicker({ childViewModel.onBirthdayChange(it) })

            Spacer(Modifier.padding(vertical = 20.dp))

            Button(
                onClick = { childViewModel.addChild() },
                enabled = !childState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (childState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Сохранить",
                        style = PlanetChildAppTypography.titleSmall,
                    )
                }
            }
            val context = LocalContext.current

            if (childState.error != null) {
                Toast.makeText(
                    context, childState.error,
                    Toast.LENGTH_LONG
                ).show()
            }

            LaunchedEffect(childState.isSuccess) {
                if (childState.isSuccess) {
                    Toast.makeText(context, "Ребенок добавлен успешно!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

@Composable
fun BirthDayPicker(onDateChange: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf("Выбрать") }

    // Состояние выбора даты (по умолчанию — сегодня)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    Column {
        // Кнопка для открытия диалогового окна выбора даты
        Button(onClick = { showDatePicker = true }) {
            Text(selectedDateText)
        }

        // Диалоговое окно с DatePicker
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        // Получаем выбранную дату и форматируем её
                        val selectedDate = datePickerState.selectedDateMillis
                        selectedDate?.let {
                            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            selectedDateText = formatter.format(it)
                            onDateChange(selectedDateText)
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewAddChildScreen() {
    AddChildScreen(
        rememberNavController(),
        viewModel(),
    )
}
