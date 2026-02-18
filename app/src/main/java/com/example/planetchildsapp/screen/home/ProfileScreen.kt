package com.example.planetchildsapp.screen.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planetchildsapp.R
import com.example.planetchildsapp.utils.loadImageBitmapFromPath
import com.example.planetchildsapp.view_model.ProfileState
import com.example.planetchildsapp.view_model.ProfileViewModel

@Composable
fun ProfileScreen(
    navHost: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val profileState by profileViewModel.profileState.collectAsState()
    
    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            profileViewModel.onAvatarSelected(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Аватар с возможностью клика
        AvatarWithPicker(
            profileState = profileState,
            onPickerClick = { pickImageLauncher.launch("image/*") }
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Имя пользователя
        if (profileState.name.isNotEmpty()) {
            Text(
                text = profileState.name,
                fontSize = 24.sp
            )
        }
        
        // Email
        if (profileState.email.isNotEmpty()) {
            Text(
                text = profileState.email,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Кнопка добавления аватара
        Button(onClick = { pickImageLauncher.launch("image/*") }) {
            Text("Изменить аватар")
        }
        
        // Отображение ошибки
        profileState.error?.let { error ->
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Компонент аватара с поддержкой загрузки из файла
 */
@Composable
private fun AvatarWithPicker(
    profileState: ProfileState,
    onPickerClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable { onPickerClick() },
        contentAlignment = Alignment.Center
    ) {
        // Загружаем изображение из файла если оно есть
        val avatarBitmap: ImageBitmap? = loadImageBitmapFromPath(profileState.avatarPath)
        
        if (avatarBitmap != null) {
            // Отображаем загруженный аватар
            Image(
                bitmap = avatarBitmap,
                contentDescription = "Аватар пользователя",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Отображаем иконку по умолчанию
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color.LightGray.copy(alpha = 0.3f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.account_circle),
                        contentDescription = "Профиль по умолчанию",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }
        
        // Иконка добавления в углу
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(36.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить фото",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(
        navController,
        viewModel()
    )
}
