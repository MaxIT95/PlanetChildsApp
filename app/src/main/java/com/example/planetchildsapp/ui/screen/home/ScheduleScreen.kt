package com.example.planetchildsapp.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun ScheduleScreen(
    navHost: NavHostController,
    profileViewModel: ProfileViewModel,
) {
    Column {
        AvatarTopBar({}, profileViewModel)
        ScheduleCards()
    }
}

@Composable
fun ScheduleCards() {
    LazyColumn (modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        item {
            ScheduleCard()
            Spacer(Modifier.padding(vertical = 5.dp))
            ScheduleCard()
            Spacer(Modifier.padding(vertical = 5.dp))
            ScheduleCard()
            Spacer(Modifier.padding(vertical = 5.dp))
            ScheduleCard()
            Spacer(Modifier.padding(vertical = 5.dp))
            ScheduleCard()
            Spacer(Modifier.padding(vertical = 5.dp))
            ScheduleCard()
            Spacer(Modifier.padding(vertical = 5.dp))
          }
    }
}

@Composable
fun ScheduleCard() {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth()
    ) {
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ScheduleScreenPreview() {
    val navController = rememberNavController()
    ScheduleScreen(
        navController, viewModel()
    )
}