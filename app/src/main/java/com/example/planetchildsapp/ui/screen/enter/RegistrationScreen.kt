package com.example.planetchildsapp.ui.screen.enter

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.ui.theme.SurfaceVariantColor
import com.example.planetchildsapp.view_model.RegistrationViewModel

@SuppressLint("ResourceAsColor")
@Composable
fun RegistrationScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: RegistrationViewModel
) {
    BackgroundRegistration(paddingValues)

    Column(
        modifier = Modifier
            .padding(top = 250.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        AuthHeader()
        Spacer(Modifier.padding(20.dp))

        val state by viewModel.uiState.collectAsState()
        val errorState by viewModel.errorsUiState.collectAsState()

        CommonTextField(
            text = state.name,
            "Имя",
            {
                viewModel.onNameChange(it)
            }, errorState.isNameValid
        )

        CommonTextField(
            text = state.email,
            "Почта",
            {
                viewModel.onEmailChange(it)
            }, errorState.isEmailValid
        )

        PasswordTextField(
            text = state.password,
            {
                viewModel.onPasswordChange(it)
            }, errorState.isPasswordValid
        )

        Spacer(Modifier.padding(10.dp))
        // Роль
        var roleState by remember { mutableStateOf("Тренер") }
        RoleSelectorButtons(
            roleState, { roleState = "Тренер" },
            { roleState = "Ученик" })

        Spacer(Modifier.padding(10.dp))

        if (state.errorMessage != null) {
            Text(text = state.errorMessage!!, color = Color.Red,
                style = PlanetChildAppTypography.bodyLarge,
                modifier = Modifier.padding(20.dp))
        }

        Button(
            {
                if (roleState == "Тренер") {
                    //todo переход на выбор услуг с передачей данных из 3 полей
                } else {
                    //todo отправляем запрос на регистрацию
                    viewModel.register()
                }
            },
            enabled = !state.isLoading,
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Продолжить",
                    style = PlanetChildAppTypography.displayLarge,
                )
            }
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
            Text(
                "Тренер",
                style = PlanetChildAppTypography.titleSmall
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = roleState == "Ученик",
                onClick = onClickStudy
            )
            Text(
                "Ученик",
                style = PlanetChildAppTypography.titleSmall
            )
        }
    }
}

@Composable
fun CommonTextField(
    text: String, label: String, onValueChange: (String) -> Unit,
    isError: Boolean
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
        isError = !isError,
        modifier = Modifier
            .padding(start = 30.dp, top = 10.dp, end = 30.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PasswordTextField(
    text: String, onValueChange: (String) -> Unit,
    isError: Boolean
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        singleLine = true,
        label = {
            Text("Пароль")
        },
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            errorContainerColor = Color.White,
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = SurfaceVariantColor,
            unfocusedContainerColor = Color.White,
            focusedLabelColor = SurfaceVariantColor,
            focusedContainerColor = Color.White
        ),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Default.Visibility
            else
                Icons.Default.VisibilityOff

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        isError = !isError,
        modifier = Modifier
            .padding(start = 30.dp, top = 10.dp, end = 30.dp)
            .fillMaxWidth()
    )
}

//
//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun RegistrationScreenPreview() {
//    PlanetChildsAppTheme {
//        Scaffold { paddingValues ->
//            RegistrationScreen(paddingValues, {},  )
//        }
//    }
//}
