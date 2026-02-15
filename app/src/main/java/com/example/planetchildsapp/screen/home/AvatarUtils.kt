package com.example.planetchildsapp.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun AvatarTopBar(
    onClick: () -> Unit, profileViewModel: ProfileViewModel
) {

    val userInfo by profileViewModel.profileState.collectAsState()

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(45.dp)
    ) {
        AvatarIcon(onClick, userInfo.avatarUri)

        Spacer(Modifier.padding(horizontal = 5.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.height(45.dp)
        ) {
            Text(userInfo.name, style = PlanetChildAppTypography.bodyMedium)
            Text(userInfo.email, style = PlanetChildAppTypography.bodySmall)
        }
    }

}

@Composable
private fun AvatarIcon(onClick: () -> Unit, avatarId: Int) {
    Surface(
        modifier = Modifier
            .size(45.dp),
        shape = CircleShape,
        color = Color.LightGray.copy(alpha = 0.3f)
    ) {
        IconButton(
            onClick = onClick,
        )
        {
            Icon(
                painter = painterResource(avatarId),
                contentDescription = "Профиль",
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AvatarTopBarPreview() {
    AvatarTopBar({}, viewModel())
}