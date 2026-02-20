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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.theme.Orange
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.ui.theme.PlanetChildsAppTheme
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
        AuthHeader()
        Spacer(Modifier.padding(20.dp))
        CommonTextHeader("Логин", 30)

        val state by authViewModel.uiState.collectAsState()
        val errorState by authViewModel.errorsUiState.collectAsState()

        LoginField(state.login, {
            authViewModel.onLoginChange(it)
        }, errorState.isLoginValid)

        Spacer(Modifier.padding(15.dp))
        CommonTextHeader("Пароль", 30)

        PasswordField(state.password, {

            authViewModel.onPasswordChange(it)
        }, errorState.isPasswordValid)
        Spacer(Modifier.padding(15.dp))

        ForgotPasswordButton()

        LoginButton(authViewModel, state, navHost)
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
fun AuthHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column {
            CommonTextButton("Войти")
            UnderLinerForText(100)
        }
    }
}

@Composable
fun CommonTextButton(
    text: String
) {
    Text(
        text = text,
        style = PlanetChildAppTypography.titleMedium,
        modifier = Modifier
            .padding(start = 30.dp)
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

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = onValueChange,
        value = text,
        singleLine = true, label = {
            Text("Введите пароль")
        }, leadingIcon = {
            Icon(
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
                    tint = MaterialTheme.colorScheme.secondary,
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
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
        isError = !isPasswordValid,
        modifier = Modifier
            .padding(start = 30.dp, top = 10.dp, end = 30.dp)
            .fillMaxWidth()
    )
}

@Composable
fun LoginButton(viewModel: AuthorizationViewModel, state: AuthorizationState, navHostController: NavHostController) {
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
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, "Авторизация прошла успешно", Toast.LENGTH_SHORT).show()
            navHostController.navigate(Destination.Home.route) {
                popUpTo(Destination.Authorization.route) { inclusive = true }
                launchSingleTop = true
            }
            //переход
            delay(400)
            viewModel.clearUiState()
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AuthorizationScreenPreview() {
    val navController = rememberNavController()

    PlanetChildsAppTheme {
        Scaffold { paddingValues ->
            AuthorizationScreen(
                paddingValues, navController,
                viewModel()
            )
        }
    }
}