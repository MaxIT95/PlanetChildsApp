package com.example.planetchildsapp.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileScreen(navHost: NavHostController) {
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenPreview() {
    val navController = rememberNavController()

    ProfileScreen(navController)
}