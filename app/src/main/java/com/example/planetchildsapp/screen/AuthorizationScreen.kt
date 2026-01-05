package com.example.planetchildsapp.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.sp
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.theme.Orange
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.ui.theme.PlanetChildsAppTheme
import com.example.planetchildsapp.ui.theme.SurfaceVariantColor

@SuppressLint("ResourceAsColor")
@Composable
fun AuthorizationScreen(paddingValues: PaddingValues) {
    Background(paddingValues)

    Column(
        modifier = Modifier
            .padding(top = 300.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        AuthAndRegistrationTextButton()
        Spacer(Modifier.padding(20.dp))
        CommonTextHeader("Логин", 30)

        var login by remember { mutableStateOf("") }
        var isLoginValid by remember { mutableStateOf(true) }

        LoginField(login, {
            login = it
            isLoginValid = login.isNotEmpty()
        }, isLoginValid)

        Spacer(Modifier.padding(15.dp))
        CommonTextHeader("Пароль", 30)

        var password by remember { mutableStateOf("") }
        var isPasswordValid by remember { mutableStateOf(true) }

        PasswordField(password, {
            password = it.filter { it.isDigit() }
            password = if (it.length <= 4) it else it.take(4)
            isPasswordValid = password.length == 4
        }, isPasswordValid)
        Spacer(Modifier.padding(15.dp))

        ForgotPasswordButton()

        LoginButton({
            if (password.length != 4) {
                isPasswordValid = false
            }
            if (login.isEmpty()) {
                isLoginValid = false
            }
            if (isPasswordValid && isLoginValid) {
                //TODO через viewModel собираем запрос на бэк (регистрация)
                Log.i("******", "go!!!")
            }
        })
    }
}

@Composable
fun ForgotPasswordButton() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "Забыли пароль?",
            fontSize = 15.sp
        )
        Spacer(Modifier.padding(start = 10.dp))
        Text(
            text = "Восстановить",
            color = Orange,
            fontSize = 15.sp,
            modifier = Modifier.clickable {

            })
    }
}

@Composable
fun Background(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .fillMaxSize()
    ) {
        Image(
            contentDescription = "фон",
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.login_background),
        )
        Image(
            contentDescription = "фон",
            modifier = Modifier
                .width(250.dp)
                .height(240.dp)
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            painter = painterResource(R.drawable.small_icon_reg),
        )
    }
}

@Composable
fun AuthAndRegistrationTextButton() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column {
            CommonTextButton("Войти", {})
            UnderLinerForText(100)
        }

        Spacer(Modifier.width(100.dp))

        Column(modifier = Modifier.padding(end = 10.dp)) {
            CommonTextButton("Регистрация", {
                //TODO добавить переход на экран регистрации
            })
        }
    }
}

@Composable
fun CommonTextButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = PlanetChildAppTypography.titleMedium,
        modifier = Modifier
            .padding(start = 30.dp)
            .clickable(
                onClick = onClick
            )
    )
}

@Composable
fun UnderLinerForText(length: Int) {
    Image(
        contentDescription = "линия",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(length.dp)
            .height(4.dp)
            .padding(start = 30.dp),
        painter = painterResource(R.drawable.login_line),
    )
}

@Composable
fun CommonTextHeader(text: String, startPadding: Int) {
    Text(
        text = text,
        modifier = Modifier.padding(start = startPadding.dp),
        style = PlanetChildAppTypography.titleSmall
    )
}

@Composable
fun LoginField(text: String, onValueChange: (String) -> Unit, isValueValid: Boolean) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        singleLine = true,
        label = {
            Text("Введите логин/эл. почту")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Icon for login field"
            )
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
fun PasswordField(text: String, onValueChange: (String) -> Unit, isPasswordValid: Boolean) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        singleLine = true, label = {
            Text("Введите пароль (4 цифры)")
        }, leadingIcon = {
            Icon(
                imageVector = Icons.Default.Key,
                contentDescription = "Icon for login field"
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            errorContainerColor = Color.White,
            unfocusedIndicatorColor = Color.LightGray,
//            focusedIndicatorColor = Color(R.color.second_gray),
            focusedIndicatorColor = SurfaceVariantColor,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
        ),
        isError = !isPasswordValid,
        modifier = Modifier
            .padding(start = 30.dp, top = 10.dp, end = 30.dp)
            .fillMaxWidth()
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = "Войти",
                fontSize = 25.sp,
            )
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AuthorizationScreenPreview() {
    PlanetChildsAppTheme {
        Scaffold { paddingValues ->
            AuthorizationScreen(paddingValues)
        }
    }
}