package com.example.planetchildsapp.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.navigation.BottomNavigation
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.screen.profile.EditProfileScreen
import com.example.planetchildsapp.ui.screen.event.EventScreen
import com.example.planetchildsapp.ui.screen.profile.ProfileScreen
import com.example.planetchildsapp.ui.screen.home.ScheduleScreen
import com.example.planetchildsapp.ui.screen.profile.AddChildScreen
import com.example.planetchildsapp.view_model.ChildViewModel
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun HomeScreenWithBottomBar(oldNavHost: NavHostController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val childViewModel: ChildViewModel = hiltViewModel()
    val profileState by profileViewModel.profileState.collectAsState()
    val isCoach = profileState.role == "COACH"
    val shouldWaitForRole = profileState.isLoading && profileState.role.isBlank()
    val homeStartDestination = if (isCoach) Destination.Clients.route else Destination.Schedule.route

    LaunchedEffect(isCoach, currentRoute, shouldWaitForRole) {

        if (!shouldWaitForRole && isCoach &&
            (currentRoute == Destination.Schedule.route || currentRoute == Destination.Events.route)
        ) {
            navController.navigate(Destination.Schedule.route) {
                popUpTo(Destination.Clients.route)
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            )
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .padding(
                    bottom = 15.dp
                )
                .fillMaxSize(),
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        val mod = Modifier
        Scaffold(
            bottomBar = {
                if (!shouldWaitForRole) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = mod
                            .navigationBarsPadding()
                            .fillMaxWidth()
                    ) {
                        BottomNavigation(
                            navHostController = navController,
                            currentRoute = currentRoute,
                            modifier = mod,
                            isCoach = isCoach
                        )
                    }
                }
            },
            containerColor = Color.Transparent,
        ) { innerPadding ->
            if (shouldWaitForRole) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                NavHost(
                    navController = navController,
                    startDestination = homeStartDestination,
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    composable(Destination.Schedule.route) {
                        ScheduleScreen(
                            navController,
                            profileViewModel
                        )
                    }

                    composable(Destination.Events.route) {
                        EventScreen(navController, profileViewModel)
                    }

                    composable(Destination.Clients.route) {
                        CoachClientsScreen(profileState)
                    }

                    composable(Destination.Profile.route) {
                        ProfileScreen(
                            navController,
                            profileViewModel
                        )
                    }

                    composable(route = Destination.EditProfile.route) {
                        EditProfileScreen(
                            navController, oldNavHost,
                            profileViewModel
                        )
                    }

                    composable(route = Destination.AddChild.route) {
                        AddChildScreen(navController, childViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun CoachClientsScreen(profileState: com.example.planetchildsapp.view_model.ProfileState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AvatarTopBar(profileState)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Список клиентов тренера пока пуст",
                color = Color.DarkGray
            )
        }
    }
}