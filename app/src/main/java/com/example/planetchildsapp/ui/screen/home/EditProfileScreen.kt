package com.example.planetchildsapp.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun EditProfileScreen(
    navHost: NavHostController,
    oldNavHost: NavHostController,
    profileViewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .padding(5.dp),
    ) {
        BackStackAndLogout(navHost, oldNavHost, profileViewModel)
        EditCard()
    }
}

@Composable
fun BackStackAndLogout(
    navHost: NavHostController,
    oldNavHost: NavHostController,
    profileViewModel: ProfileViewModel
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
                modifier = Modifier
                    .size(30.dp),
                painter = painterResource(R.drawable.keyboard_backspace),
                contentDescription = ""
            )
            Text(
                text = "Назад",
                style = PlanetChildAppTypography.bodyMedium,
            )
        }
        Logout(oldNavHost, profileViewModel)
    }
}


@Composable
fun EditCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "Профиль",
                style = PlanetChildAppTypography.headlineMedium,
                textAlign = TextAlign.Center
            )

        }

    }
}

@Composable
fun Logout(
    navHost: NavHostController,
    profileViewModel: ProfileViewModel
) {
    IconButton(
        onClick = {
            navHost.navigate(Destination.Enter.route)
            profileViewModel.logout()
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
fun EditProfileScreenPreview() {
    EditProfileScreen(
        rememberNavController(), rememberNavController(),
        viewModel()
    )
}
