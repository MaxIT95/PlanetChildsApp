package com.example.planetchildsapp.navigation

import HomeScreenWithBottomBar
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.planetchildsapp.screen.enter.AuthorizationScreen
import com.example.planetchildsapp.screen.enter.EnterScreen
import com.example.planetchildsapp.screen.enter.GreetingScreen
import com.example.planetchildsapp.screen.enter.RegistrationScreen
import com.example.planetchildsapp.screen.enter.SplashScreen
import com.example.planetchildsapp.view_model.AuthorizationViewModel
import com.example.planetchildsapp.view_model.RegistrationViewModel
import kotlinx.coroutines.delay

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
) {
    NavHost(
        navController = navHostController,
        startDestination = Destination.Splash.route
    ) {
        // Новый Splash экран с проверкой токена
        composable(route = Destination.Splash.route) {
            val viewModel: AuthorizationViewModel = hiltViewModel()
            val isAuthenticated by viewModel.resultAuth.collectAsState()

            // Запускаем проверку токена при загрузке экрана
            LaunchedEffect(Unit) {
                viewModel.checkActualToken()
            }
            
            // Навигация в зависимости от результата
            LaunchedEffect(isAuthenticated) {
                delay(2000)
//                if (isAuthenticated) {
//                    navHostController.navigate(Destination.Home.route) {
//                        popUpTo(Destination.Splash.route) { inclusive = true }
//                    }
//                } else {
                    navHostController.navigate(Destination.Greeting.route) {
                        popUpTo(Destination.Splash.route) { inclusive = true }
//                    }
                }
            }
            // Показываем Splash UI во время проверки
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