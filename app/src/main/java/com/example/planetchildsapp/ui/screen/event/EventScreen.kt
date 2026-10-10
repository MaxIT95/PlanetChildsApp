package com.example.planetchildsapp.ui.screen.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.screen.home.AvatarTopBar
import com.example.planetchildsapp.ui.screen.home.model.EventModel
import com.example.planetchildsapp.view_model.EventViewModel
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun EventScreen(
    navHost: NavHostController,
    profileViewModel: ProfileViewModel,
    eventViewModel: EventViewModel = hiltViewModel(),
) {
    val userInfo by profileViewModel.profileState.collectAsState()
    val eventState by eventViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AvatarTopBar(userInfo)

        when {
            eventState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            eventState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = eventState.error ?: "Ошибка загрузки мероприятий",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            eventState.events.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Мероприятия пока отсутствуют",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            else -> {
                EventCards(
                    events = eventState.events,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EventCards(
    events: List<EventModel>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = events, key = { it.id }) { event ->
            EventCard(event)
        }
    }
}

@Composable
private fun EventCard(event: EventModel) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                model = event.picturePath?.takeIf { it.isNotBlank() } ?: R.drawable.background,
                placeholder = painterResource(R.drawable.background),
                error = painterResource(R.drawable.background),
                contentDescription = event.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                event.eventDate?.takeIf { it.isNotBlank() }?.let { date ->
                    Text(
                        text = "Дата: $date",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}