package com.example.planetchildsapp.ui.screen.event

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.ui.screen.home.AvatarTopBar
import com.example.planetchildsapp.ui.screen.home.ScheduleCards
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun EventScreen(
    navHost: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val userInfo by profileViewModel.profileState.collectAsState()

    Column {
        AvatarTopBar(userInfo)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun EventScreenPreview() {
    val navController = rememberNavController()
    EventScreen(navController, viewModel())
}