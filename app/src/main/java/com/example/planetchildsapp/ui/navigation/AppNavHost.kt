package com.example.planetchildsapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.planetchildsapp.ui.screen.enter.AuthorizationScreen
import com.example.planetchildsapp.ui.screen.enter.EnterScreen
import com.example.planetchildsapp.ui.screen.enter.GreetingScreen
import com.example.planetchildsapp.ui.screen.enter.RegistrationScreen
import com.example.planetchildsapp.ui.screen.enter.SplashScreen
import com.example.planetchildsapp.ui.screen.home.HomeScreenWithBottomBar
import com.example.planetchildsapp.view_model.AuthorizationViewModel
import com.example.planetchildsapp.view_model.RegistrationViewModel
import kotlinx.coroutines.delay

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navHostController,
        startDestination = Destination.Splash.route
    ) {
        // Новый Splash экран с проверкой токена
        composable(route = Destination.Splash.route) {
            val viewModel: AuthorizationViewModel = hiltViewModel()
            val isAuthenticated by viewModel.resultAuth.collectAsState()
            val isAuthChecking by viewModel.isAuthChecking.collectAsState()
            var isSplashDelayFinished by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                viewModel.checkActualToken()
            }

            LaunchedEffect(Unit) {
                delay(2000)
                isSplashDelayFinished = true
            }

            LaunchedEffect(isAuthChecking, isAuthenticated, isSplashDelayFinished) {
                if (!isAuthChecking && isSplashDelayFinished) {
                    val destination =
                        if (isAuthenticated) Destination.Home.route else Destination.Greeting.route

                    navHostController.navigate(destination) {
                        popUpTo(Destination.Splash.route) { inclusive = true }
                    }
                }
            }

            SplashScreen()
        }

        // Обычный Greeting экран (без логики проверки токена)
        composable(route = Destination.Greeting.route) {
            GreetingScreen(navHostController, paddingValues)
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
            val registrationViewModel: RegistrationViewModel = hiltViewModel(backStackEntry)

            RegistrationScreen(
                paddingValues, navHostController, registrationViewModel
            )
        }

        composable(route = Destination.Home.route) {
            HomeScreenWithBottomBar(navHostController)
        }
    }
}