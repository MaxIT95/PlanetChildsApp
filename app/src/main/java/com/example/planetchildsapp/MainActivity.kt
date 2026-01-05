package com.example.planetchildsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.navigation.AppNavHost
import com.example.planetchildsapp.ui.theme.PlanetChildsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanetChildsAppTheme() {
                Scaffold { paddingValues ->
                    val navHost = rememberNavController()
                    AppNavHost(navHost, paddingValues)
                }
            }
        }
    }
}