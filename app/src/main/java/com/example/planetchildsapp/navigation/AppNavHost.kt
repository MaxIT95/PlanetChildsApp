package com.example.planetchildsapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.planetchildsapp.screen.AuthorizationScreen
import com.example.planetchildsapp.screen.GreetingScreen
import com.example.planetchildsapp.screen.RegistrationScreen

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navHostController,
        startDestination = Destination.Greeting.route
    ) {
        composable(route = Destination.Greeting.route) {
            GreetingScreen(
                onNextClick = {
                    navHostController.navigate(Destination.Authorization.route)
                }, paddingValues
            )
        }
        composable(route = Destination.Authorization.route) {
            AuthorizationScreen(
                paddingValues,

                onRegistrationClick = {
                    navHostController.navigate(Destination.Registration.route)
                })
        }
        composable(route = Destination.Registration.route) {
            RegistrationScreen(
                paddingValues,
                {
                    navHostController.navigate(Destination.Authorization.route)
                })
        }
    }
}