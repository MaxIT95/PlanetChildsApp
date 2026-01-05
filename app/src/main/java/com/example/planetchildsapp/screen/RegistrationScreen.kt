package com.example.planetchildsapp.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.ui.theme.PlanetChildsAppTheme
import com.example.planetchildsapp.ui.theme.SurfaceVariantColor

@SuppressLint("ResourceAsColor")
@Composable
fun RegistrationScreen(
    paddingValues: PaddingValues,
    onAuthorizationClick: () -> Unit
) {
    BackgroundRegistration(paddingValues)

    Column(
        modifier = Modifier
            .padding(top = 250.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        AuthAndRegistrationTextButton(true, {}, onAuthorizationClick)
        Spacer(Modifier.padding(20.dp))

        //имя
        var name by remember { mutableStateOf("") }
        var isNameValid by remember { mutableStateOf(true) }
        CommonTextField(name, "Имя", {}, isNameValid)

        //почта
        var mail by remember { mutableStateOf("") }
        var isMailValid by remember { mutableStateOf(true) }
        CommonTextField(mail, "Почта", {}, isMailValid)

        //пароль
        var password by remember { mutableStateOf("") }
        var isPasswordValid by remember { mutableStateOf(true) }
        CommonTextField(password, "Пароль (4 цифры)", {}, isPasswordValid)

        Spacer(Modifier.padding(10.dp))
        // Роль
        var roleState by remember { mutableStateOf("Тренер") }
        RoleSelectorButtons(
            roleState, { roleState = "Тренер" },
            { roleState = "Ученик" })

        Spacer(Modifier.padding(10.dp))

        Button(
            {},
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Продолжить",
                style = PlanetChildAppTypography.displayLarge,
            )
        }

    }
}

@Composable
fun BackgroundRegistration(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()
    ) {
        Image(
            contentDescription = "фон",
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.login_background),
        )
        Image(
            contentDescription = "фон",
            modifier = Modifier
                .width(250.dp)
                .height(200.dp)
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            painter = painterResource(R.drawable.small_icon_reg),
        )
    }
}

@Composable
fun RoleSelectorButtons(
    roleState: String, onClickTrainer: () -> Unit,
    onClickStudy: () -> Unit
) {
    Column(modifier = Modifier.padding(start = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = roleState == "Тренер",
                onClick = onClickTrainer
            )
            Text("Тренер")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = roleState == "Ученик",
                onClick = onClickStudy
            )
            Text("Ученик")
        }
    }
}

@Composable
fun CommonTextField(
    text: String, label: String, onValueChange: (String) -> Unit,
    isValueValid: Boolean
) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        singleLine = true,
        label = {
            Text(label)
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            errorContainerColor = Color.White,
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = SurfaceVariantColor,
            unfocusedContainerColor = Color.White,
            focusedLabelColor = SurfaceVariantColor,
            focusedContainerColor = Color.White
        ),
        isError = !isValueValid,
        modifier = Modifier
            .padding(start = 30.dp, top = 10.dp, end = 30.dp)
            .fillMaxWidth()
    )
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun RegistrationScreenPreview() {
    PlanetChildsAppTheme {
        Scaffold { paddingValues ->
            RegistrationScreen(paddingValues, {})
        }
    }
}
