package com.example.planetchildsapp.screen.enter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.navigation.Destination
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.ui.theme.PlanetChildsAppTheme

@Composable
fun GreetingScreen(
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
        MainHeader()
        Spacer(Modifier.padding(vertical = 15.dp))
        ContinueArrowButton(navHost)
    }
}

@Composable
fun MainBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            contentDescription = "фон",
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.greeting_background),
        )
    }
}

@Composable
fun MainHeader() {
    Text(
        text = "Добро пожаловать в Планету Детства!",
        textAlign = TextAlign.Center,
        color = Color.Black,
        style = PlanetChildAppTypography.headlineMedium
    )
}

@Composable
fun ContinueArrowButton(navHost: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Продолжить",
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            style = PlanetChildAppTypography.headlineSmall
        )
        Spacer(Modifier.padding(horizontal = 20.dp))
        IconButton(
            onClick = {
                navHost.navigate(Destination.Enter.route)
            },
            modifier = Modifier
                .size(100.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_continue),
                contentDescription = "Icon Button"
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun GreetingScreenPreview() {
    PlanetChildsAppTheme {
        val navController = rememberNavController()
        GreetingScreen(navController, PaddingValues(0.dp))
    }
}