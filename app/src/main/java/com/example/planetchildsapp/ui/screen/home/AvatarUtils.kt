package com.example.planetchildsapp.ui.screen.home

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.planetchildsapp.R
import com.example.planetchildsapp.ui.theme.Orange
import com.example.planetchildsapp.ui.theme.PlanetChildAppTypography
import com.example.planetchildsapp.view_model.ProfileState

@Composable
fun AvatarTopBar(
    profileState: ProfileState,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Avatar(
            avatarPath = profileState.avatarPath,
            onAvatarSelected = {},
            clickable = false,
            imageSize = 80.dp
        )

        Spacer(Modifier.padding(horizontal = 5.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.height(100.dp)
        ) {
            Text(profileState.name, style = PlanetChildAppTypography.labelSmall)
            Text(profileState.email, style = PlanetChildAppTypography.bodyMedium)
        }
    }

}

@Composable
fun Avatar(
    avatarPath: String?,
    onAvatarSelected: (Uri) -> Unit,
    imageSize: Dp = 120.dp,
    clickable: Boolean = true
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAvatarSelected(it) }
    }

    Box(
        modifier = Modifier
            .clickable(
                enabled = clickable,
                onClick = { launcher.launch("image/*") })
            .border(4.dp, Orange, shape = CircleShape)
            .clip(CircleShape),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(avatarPath ?: R.drawable.account_circle)
                .crossfade(true)
                .transformations(CircleCropTransformation())
                .listener(
                    onStart = { request ->
                        Log.d("Coil", "Loading started, data=${request.data}")
                    },
                    onSuccess = { request, result ->
                        Log.d(
                            "Coil",
                            "Loaded OK, data=${request.data}, source=${result.dataSource}"
                        )
                    },
                    onError = { request, result ->
                        Log.e("Coil", "Load FAILED, data=${request.data}", result.throwable)
                    }
                )
                .build(),
            placeholder = painterResource(R.drawable.avatar),
            contentDescription = "Аватар пользователя",
            error = painterResource(R.drawable.logout),
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AvatarTopBarPreview() {
    AvatarTopBar(ProfileState(avatarPath = null))
}