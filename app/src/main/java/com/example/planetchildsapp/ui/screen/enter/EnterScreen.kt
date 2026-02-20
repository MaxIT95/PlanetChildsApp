package com.example.planetchildsapp.ui.screen.enter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.theme.PlanetChildsAppTheme

@Composable
fun EnterScreen(
    navHost: NavHostController,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        MainBackground()
        Spacer(Modifier.padding(vertical = 10.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navHost.navigate(Destination.Authorization.route) },
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp)
            ) {
                Text("Войти")
            }
            Spacer(Modifier.padding(vertical = 10.dp))
            Button(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                onClick = { navHost.navigate(Destination.Registration.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp)
            ) {
                Text(
                    "Регистрация",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun EnterScreenPreview() {
    PlanetChildsAppTheme {

        val navController = rememberNavController()
        EnterScreen(navController, PaddingValues(0.dp))
    }
}