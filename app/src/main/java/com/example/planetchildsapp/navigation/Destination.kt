package com.example.planetchildsapp.navigation

sealed class Destination(val route: String) {

    data object Splash : Destination(ROUTE_SPLASH)
    data object Greeting : Destination(ROUTE_GREETING)
    data object Enter : Destination(ROUTE_ENTER)
    data object Authorization : Destination(ROUTE_AUTHORIZATION)
    data object Registration : Destination(ROUTE_REGISTRATION)
    data object Schedule : Destination(ROUTE_SCHEDULE)
    data object Events : Destination(ROUTE_EVENTS)
    data object Profile : Destination(ROUTE_PROFILE)
    data object Home : Destination(ROUTE_HOME)

    companion object {
        private const val ROUTE_SPLASH = "route_splash"
        private const val ROUTE_GREETING = "route_greeting"
        private const val ROUTE_ENTER = "route_enter"
        private const val ROUTE_AUTHORIZATION = "route_authorization"
        private const val ROUTE_REGISTRATION = "route_registration"
        private const val ROUTE_SCHEDULE = "route_schedule"
        private const val ROUTE_EVENTS = "route_events"
        private const val ROUTE_PROFILE = "route_profile"
        private const val ROUTE_HOME = "route_home"
    }
}