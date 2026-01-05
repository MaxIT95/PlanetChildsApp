package com.example.planetchildsapp.navigation

sealed class Destination(val route: String) {

    data object Greeting : Destination(ROUTE_GREETING)
    data object Authorization: Destination(ROUTE_AUTHORIZATION)
    data object Registration: Destination(ROUTE_REGISTRATION)

    companion object {
        private const val ROUTE_GREETING = "route_greeting"
        private const val ROUTE_AUTHORIZATION = "route_authorization"
        private const val ROUTE_REGISTRATION = "route_registration"
    }
}