package com.example.planetchildsapp.ui.screen.profile

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.screen.home.Avatar
import com.example.planetchildsapp.ui.theme.Dimensions.paddingMedium
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.view_model.ChildViewModel
import com.example.planetchildsapp.view_model.ProfileState
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun ProfileScreen(
    navHost: NavHostController,
    profileViewModel: ProfileViewModel,
    childViewModel: ChildViewModel
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
        Spacer(Modifier.padding(vertical = 5.dp))
//        ChildrenView(childViewModel)
        Spacer(Modifier.padding(vertical = 10.dp))
        AddChildButton({ navHost.navigate(Destination.AddChild.route) })
    }
}

@Composable
fun UserInfoRow(
    profileState: ProfileState,
    navHost: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(
            avatarPath = profileState.avatarPath,
            onAvatarSelected = {}
        )

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

//@Composable
//fun ChildrenView(childViewModel: ChildViewModel) {
//    val childState by childViewModel.chil.collectAsState()
//}

@Composable
fun AddChildButton(onClick: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = ""
                )
                Spacer(Modifier.padding(horizontal = 5.dp))
                Text(
                    text = "Добавить ребёнка",
                    style = PlanetChildAppTypography.labelSmall
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenPreview() {
    AddChildButton({})
}
