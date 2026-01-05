package com.example.planetchildsapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.planetchildsapp.screen.AuthorizationScreen
import com.example.planetchildsapp.screen.GreetingScreen

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
//                onNextClick = {
//                navHostController.navigate(Destination.Authorization.route)
//            },
                paddingValues)
        }
    }
}