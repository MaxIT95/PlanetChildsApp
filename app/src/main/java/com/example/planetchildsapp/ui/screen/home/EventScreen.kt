package com.example.planetchildsapp.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun EventScreen(navHost: NavHostController) {
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun EventScreenPreview() {
    val navController = rememberNavController()
    EventScreen(navController)
}