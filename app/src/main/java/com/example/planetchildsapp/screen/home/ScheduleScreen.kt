package com.example.planetchildsapp.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ScheduleScreen(
    navHost: NavHostController,
    paddingValues: PaddingValues
) {

}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScheduleScreenPreview() {
    val navController = rememberNavController()
    ScheduleScreen(navController, PaddingValues(16.dp))
}