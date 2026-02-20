import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.navigation.BottomNavigation
import com.example.planetchildsapp.ui.navigation.Destination
import com.example.planetchildsapp.ui.screen.home.EditProfileScreen
import com.example.planetchildsapp.ui.screen.home.EventScreen
import com.example.planetchildsapp.ui.screen.home.ProfileScreen
import com.example.planetchildsapp.ui.screen.home.ScheduleScreen

@Composable
fun HomeScreenWithBottomBar(oldNavHost: NavHostController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
            )
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .padding(
                    bottom = 15.dp
                )
                .fillMaxSize(),
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop // максимально растягивает
        )

        val mod = Modifier
        Scaffold(
            bottomBar = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = mod
                        .navigationBarsPadding()
                        .fillMaxWidth()
                ) {
                    BottomNavigation(
                        navHostController = navController,
                        currentRoute = currentRoute,
                        modifier = mod
                    )
                }
            },
            containerColor = Color.Transparent,
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.Schedule.route,
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                composable(Destination.Schedule.route) {
                    ScheduleScreen(
                        navController,
                        hiltViewModel()
                    )
                }

                composable(Destination.Events.route) {
                    EventScreen(navController)
                }

                composable(Destination.Profile.route) {
                    ProfileScreen(navController, hiltViewModel())
                }

                composable(route = Destination.EditProfile.route) {
                    EditProfileScreen(navController, oldNavHost, hiltViewModel())
                }
            }
        }
    }
}