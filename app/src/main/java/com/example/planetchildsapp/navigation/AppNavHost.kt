package com.example.planetchildsapp.navigation

import HomeScreenWithBottomBar
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.planetchildsapp.screen.enter.AuthorizationScreen
import com.example.planetchildsapp.screen.enter.EnterScreen
import com.example.planetchildsapp.screen.enter.GreetingScreen
import com.example.planetchildsapp.screen.enter.RegistrationScreen
import com.example.planetchildsapp.view_model.RegistrationViewModel

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navHostController,
//        startDestination = Destination.Greeting.route
        startDestination = Destination.Home.route
    ) {

        composable(route = Destination.Greeting.route) {
            GreetingScreen(
                navHostController, paddingValues
            )
        }

        composable(route = Destination.Enter.route) {
            EnterScreen(
                navHostController,
                paddingValues
            )
        }

        composable(route = Destination.Authorization.route) {
            AuthorizationScreen(
                paddingValues, navHostController, hiltViewModel()
            )
        }

        composable(route = Destination.Registration.route) { backStackEntry ->
            val registrationViewModel: RegistrationViewModel = viewModel(backStackEntry)

            RegistrationScreen(
                paddingValues, navHostController, registrationViewModel
            )
        }

        composable(route = Destination.Home.route) {
            HomeScreenWithBottomBar()
        }
    }
}