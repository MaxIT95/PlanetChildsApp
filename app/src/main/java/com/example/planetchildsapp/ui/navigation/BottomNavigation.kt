package com.example.planetchildsapp.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography

@Composable
fun BottomNavigation(
    currentRoute: String?,
    navHostController: NavHostController,
    modifier: Modifier
) {
    val items = listOf(
        NavInfoButton("Расписание", R.drawable.schedule_icon, Destination.Schedule.route),
        NavInfoButton("Мероприятия", R.drawable.calendar, Destination.Events.route),
        NavInfoButton("Профиль", R.drawable.account_circle, Destination.Profile.route)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.45f)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { item ->
            NavigationButton(
                item,
                navHostController,
                currentRoute == item.route
            )
        }
    }
}

@Composable
fun NavigationButton(
    navInfo: NavInfoButton,
    navHost: NavHostController,
    selected: Boolean
) {
    val backgroundColor = if (selected) Color.LightGray.copy(alpha = 0.7f) else Color.Transparent

    Box(
        modifier = Modifier
            .height(70.dp)
            .width(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                enabled = true,
                onClick = {
                    if (!selected) {
                        navHost.navigate(navInfo.route)
                    }
                })
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(40.dp)
                    .width(60.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    painter = painterResource(navInfo.iconId),
                    contentDescription = "",
                )
            }
            Spacer(Modifier.padding(vertical = 5.dp))
            Text(
                navInfo.name,
                textAlign = TextAlign.Center,
                style = PlanetChildAppTypography.bodyMedium
            )
        }
    }
}

data class NavInfoButton(val name: String, val iconId: Int, val route: String)

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun BottomNavigationPreview() {
//    BottomNavigation("", {}, {}, {}, Modifier)
}