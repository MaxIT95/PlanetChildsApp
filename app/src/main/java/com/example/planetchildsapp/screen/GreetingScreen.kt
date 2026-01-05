package com.example.planetchildsapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planetchildsapp.R

@Composable
fun GreetingScreen(onNextClick: () -> Unit, paddingValues: PaddingValues) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        Image(
            contentDescription = "фон",
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillHeight,
            painter = painterResource(R.drawable.greeting_background),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Добро пожаловать в Планету Детства!",
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Продолжить",
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 25.dp, start = 50.dp)
                )
                IconButton(
                    onClick = onNextClick,
                    modifier = Modifier
                        .padding(start = 30.dp, bottom = 17.dp)
                        .size(100.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_continue),
                        contentDescription = "Icon Button"
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun GreetingScreenPreview() {
    GreetingScreen({}, PaddingValues(16.dp))
}