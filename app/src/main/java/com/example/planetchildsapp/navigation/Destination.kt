package com.example.planetchildsapp.navigation

sealed class Destination(val route: String) {

    data object Greeting : Destination(ROUTE_GREETING)
    data object Authorization: Destination(ROUTE_AUTHORIZATION)

    companion object {
        private const val ROUTE_GREETING = "route_greeting"
        private const val ROUTE_AUTHORIZATION = "route_authorization"
    }
}