package com.example.planetchildsapp.ui.screen.enter

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.data.model.UserStorageInfo
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.theme.Orange
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.ui.theme.SurfaceVariantColor
import com.example.planetchildsapp.view_model.AuthorizationState
import com.example.planetchildsapp.view_model.AuthorizationViewModel
import kotlinx.coroutines.delay


@SuppressLint("ResourceAsColor")
@Composable
fun AuthorizationScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    authViewModel: AuthorizationViewModel
) {
    Background(paddingValues)

    Column(
        modifier = Modifier
            .padding(top = 250.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
    }

    val state by authViewModel.uiState.collectAsState()
    val errorState by authViewModel.errorsUiState.collectAsState()

    AuthHeader("Войти", 100)
    Spacer(Modifier.padding(20.dp))

    CommonTextHeader("Логин", 30)

    EnterTextField(
        state.login, {
            authViewModel.onLoginChange(it)
        }, errorState.isLoginValid,
        "Введите логин/эл. почту",
        Icons.Default.Person
    )

    Spacer(Modifier.padding(15.dp))
    CommonTextHeader("Пароль", 30)

    PasswordField(state.password, {
        authViewModel.onPasswordChange(it)
    }, errorState.isPasswordValid, "Введите пароль")
    Spacer(Modifier.padding(15.dp))

    ForgotPasswordButton()

    LoginButton(authViewModel, state, navHost)
}
//}

@Composable
fun UsersDropDown(authViewModel: AuthorizationViewModel) {

    val users: List<UserStorageInfo> = authViewModel.
    // Выпадающий список пользователей
    DropdownMenu(
        expanded = false,
        onDismissRequest = { /* TODO */ }
    ) {
        users.forEach { user ->
            DropdownMenuItem(
                text = { Text(user.login) },
                onClick = {
                    viewModel.selectUser(user)
                    username = user.username
                    password = user.password
                }
            )
        }
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
fun AuthHeader(text: String, underline: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Column {
            CommonTextButton(text)
            Image(
                contentDescription = "линия",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(underline.dp)
                    .height(2.dp)
                    .padding(start = 21.dp),
                painter = painterResource(R.drawable.login_line),
            )
        }
    }
}

@Composable
fun CommonTextButton(
    text: String
) {
    Text(
        text = text,
        style = PlanetChildAppTypography.labelLarge,
        modifier = Modifier
            .padding(start = 20.dp)
    )
}

@Composable
fun CommonTextHeader(text: String, startPadding: Int) {
    Text(
        text = text,
        modifier = Modifier.padding(start = startPadding.dp),
        style = PlanetChildAppTypography.labelSmall
    )
}

@Composable
fun EnterTextField(
    text: String, onValueChange: (String) -> Unit,
    isValueValid: Boolean, placeholder: String,
    imageVector: ImageVector,
    horizontalPadding: Dp = 20.dp
) {
    TextField(
        onValueChange = onValueChange,
        value = text,
        singleLine = true,
        placeholder = { Text(placeholder, style = PlanetChildAppTypography.bodyMedium) },
        leadingIcon = {
            Icon(
                tint = if (!isValueValid) Color.Red else Color.DarkGray,
                imageVector = imageVector,
                contentDescription = "Icon for login field"
            )
        },
        colors = TextFieldDefaults.colors(
            errorContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Black,
            cursorColor = SurfaceVariantColor,
            focusedIndicatorColor = SurfaceVariantColor,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = SurfaceVariantColor,
            focusedContainerColor = Color.Transparent
        ),
        isError = !isValueValid,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
    )
}

@Composable
fun PasswordField(
    text: String, onValueChange: (String) -> Unit,
    isPasswordValid: Boolean,
    placeholder: String,
    horizontalPadding: Dp = 20.dp
) {

    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = onValueChange,
        value = text,
        singleLine = true,
        placeholder = {
            Text(
                placeholder,
                style = PlanetChildAppTypography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                tint = if (!isPasswordValid) Color.Red else Color.DarkGray,
                imageVector = Icons.Default.Key,
                contentDescription = "Icon for login field"
            )
        },
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Default.Visibility
            else
                Icons.Default.VisibilityOff

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    tint = Color.Black,
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            errorContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Black,
            cursorColor = SurfaceVariantColor,
            focusedIndicatorColor = SurfaceVariantColor,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = SurfaceVariantColor,
            focusedContainerColor = Color.Transparent
        ),
        isError = !isPasswordValid,
        modifier = Modifier
            .padding(horizontal = horizontalPadding)
            .fillMaxWidth()
    )
}

@Composable
fun LoginButton(
    viewModel: AuthorizationViewModel,
    state: AuthorizationState,
    navHostController: NavHostController
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = { viewModel.authorize() },
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
                .align(Alignment.Center)
        ) {

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Войти",
                    style = PlanetChildAppTypography.displayLarge,
                )
            }
        }
    }

    if (state.errorMessage != null) {
        Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, "Авторизация прошла успешно", Toast.LENGTH_SHORT).show()
            navHostController.navigate(Destination.Home.route) {
                popUpTo(Destination.Authorization.route) { inclusive = true }
            }
            //переход
            delay(400)
            viewModel.clearUiState()
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoginFieldPreview() {
    Column {
        Spacer(Modifier.padding(vertical = 100.dp))
        EnterTextField(
            "name", {}, true,
            "placeholder", Icons.Default.Person
        )
    }

}