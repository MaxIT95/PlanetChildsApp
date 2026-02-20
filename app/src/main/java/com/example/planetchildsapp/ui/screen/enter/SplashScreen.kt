package com.example.planetchildsapp.ui.screen.enter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.planetchildsapp.R

@Composable
fun SplashScreen() {

    Box(
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            )
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Логотип",
            modifier = Modifier
                .padding(
                    bottom = 15.dp
                )
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.small_icon_reg),
                contentDescription = "Логотип",
                modifier = Modifier
                    .padding(top = 200.dp)
                    .size(200.dp),
            )
            Spacer(modifier = Modifier.padding(vertical = 30.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color.LightGray
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SplashScreenPreview() {
    SplashScreen()
}
