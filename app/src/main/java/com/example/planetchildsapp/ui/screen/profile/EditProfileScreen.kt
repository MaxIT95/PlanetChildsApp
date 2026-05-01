package com.example.planetchildsapp.ui.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.screen.enter.EnterTextField
import com.example.planetchildsapp.ui.screen.enter.PasswordField
import com.example.planetchildsapp.ui.screen.home.Avatar
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.ui.theme.SurfaceVariantColor
import com.example.planetchildsapp.view_model.ProfileState
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun EditProfileScreen(
    navHost: NavHostController,
    oldNavHost: NavHostController,
    profileViewModel: ProfileViewModel
) {
    profileViewModel.clearSuccessStatus()
    val profileState by profileViewModel.profileState.collectAsState()

    Column(modifier = Modifier.padding(5.dp)) {
        BackStackAndLogout(
            navHost = navHost,
            oldNavHost = oldNavHost,
            onLogout = { profileViewModel.logout() }
        )
        EditCard(
            profileState = profileState,
            onPasswordChange = { profileViewModel.onPasswordChange(it) },
            onEmailChange = { profileViewModel.onEmailChange(it) },
            onNameChange = { profileViewModel.onNameChange(it) },
            onAvatarSelected = { profileViewModel.copyPhotoPickerUriToInternalStorage(it) },
            onSave = { profileViewModel.onSaveChanges() }
        )
    }
}

@Composable
fun BackStackAndLogout(
    navHost: NavHostController,
    oldNavHost: NavHostController,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            shape = RoundedCornerShape(12.dp),
            onClick = {
                if (navHost.previousBackStackEntry != null) {
                    navHost.popBackStack()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(R.drawable.keyboard_backspace),
                contentDescription = ""
            )
            Text(
                text = "Назад",
                style = PlanetChildAppTypography.bodyMedium,
            )
        }
        Logout(oldNavHost, onLogout)
    }
}


@Composable
fun EditCard(
    profileState: ProfileState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onAvatarSelected: (Uri) -> Unit,
    onSave: () -> Unit
) {
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
                avatarPath = profileState.avatarPath,
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
                profileState.name,
                onNameChange, profileState.isNameValid,
                "Введите ваше имя", Icons.Default.Person,
                horizontalPadding = 0.dp
            )

            Spacer(Modifier.padding(vertical = 10.dp))

            // E-mail
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "E-mail",
                textAlign = TextAlign.Start,
                style = PlanetChildAppTypography.titleSmall
            )

            EnterTextField(
                profileState.email,
                onEmailChange, profileState.isEmailValid,
                "Введите ваш e-mail", Icons.Default.Email,
                horizontalPadding = 0.dp
            )

            Spacer(Modifier.padding(vertical = 10.dp))
            // Пароль
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Пароль",
                textAlign = TextAlign.Start,
                style = PlanetChildAppTypography.titleSmall
            )

            PasswordField(
                text = profileState.password,
                onValueChange = onPasswordChange,
                isPasswordValid = profileState.isPasswordValid,
                placeholder = "Введите ваш пароль",
                horizontalPadding = 0.dp
            )

            Spacer(Modifier.padding(vertical = 20.dp))

            Button(
                onClick = onSave,
                enabled = !profileState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (profileState.isLoading) {
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

            if (profileState.error != null) {
                Toast.makeText(
                    context, profileState.error,
                    Toast.LENGTH_LONG
                ).show()
            }

            LaunchedEffect(profileState.isSuccess) {
                if (profileState.isSuccess) {
                    Toast.makeText(context, "Изменения сохранены успешно", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

@Composable
fun Logout(
    navHost: NavHostController,
    onLogout: () -> Unit
) {
    IconButton(
        onClick = {
            navHost.navigate(Destination.Enter.route)
            onLogout()
        },
        modifier = Modifier.size(30.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.logout),
            contentDescription = "выход"
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun EditCardPreview() {
    EditCard(
        profileState = ProfileState(
            name = "Иван Иванов",
            email = "ivan@example.com",
            password = "password123",
            avatarPath = null
        ),
        onNameChange = {},
        onPasswordChange = {},
        onEmailChange = {},
        onAvatarSelected = {},
        onSave = {}
    )
}
