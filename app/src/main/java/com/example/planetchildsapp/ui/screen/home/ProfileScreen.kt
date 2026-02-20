package com.example.planetchildsapp.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.theme.Dimensions.paddingMedium
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.utils.loadImageBitmapFromPath
import com.example.planetchildsapp.view_model.ProfileState
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun ProfileScreen(
    navHost: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val profileState by profileViewModel.profileState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        UserInfoRow(
            profileState = profileState,
            navHost = navHost
        )
    }
}

@Composable
fun UserInfoRow(
    profileState: ProfileState,
    navHost: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(profileState)

        Spacer(modifier = Modifier.width(paddingMedium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = profileState.name,
                style = PlanetChildAppTypography.labelMedium
            )
            Text(
                text = profileState.email,
                style = PlanetChildAppTypography.bodyMedium
            )
        }
        IconButton(
            onClick = { navHost.navigate(Destination.EditProfile.route) },
            modifier = Modifier.size(30.dp) // уменьшенный размер кнопки
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Редактировать"
            )
        }
    }
}

@Composable
fun Avatar(profileState: ProfileState) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
    ) {
        val avatarBitmap: ImageBitmap? = loadImageBitmapFromPath(profileState.avatarPath)
        if (avatarBitmap != null) {
            // Отображаем загруженный аватар
            Image(
                bitmap = avatarBitmap,
                contentDescription = "Аватар пользователя",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.account_circle),
                contentDescription = "Аватар пользователя",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenPreview() {

    val navhost = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        UserInfoRow(
            ProfileState(
                name = "Иван иванович",
                email = "kuleshov995@yandex.ru"
            ),
            navhost
        )
    }
}
