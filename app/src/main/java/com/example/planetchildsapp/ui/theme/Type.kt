package com.example.planetchildsapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PlanetChildAppTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = rubikRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp),
    headlineSmall = TextStyle(
        fontFamily = rubikRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp),
    displayLarge = TextStyle(
        fontFamily = rubikSemiBoldFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp),
    titleLarge = TextStyle(
        fontFamily = rubikSemiBoldFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp),
    titleMedium = TextStyle(
        fontFamily = rubikRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 23.sp),
    titleSmall = TextStyle(
        fontFamily = rubikRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp),
    bodyMedium = TextStyle(
        fontFamily = rubikRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp),
    bodySmall = TextStyle(
        fontFamily = rubikRegularFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp),
    labelMedium = TextStyle(
        fontFamily = rubikSemiBoldFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp),
    labelLarge = TextStyle(
        fontFamily = rubikSemiBoldFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp)
)

@Preview(showBackground = true)
@Composable
fun TypographyPreview() {
    PlanetChildsAppTheme() {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("displayLarge - Заголовки экранов", style = PlanetChildAppTypography.displayLarge)
            Text("titleMedium - Карточки", style = PlanetChildAppTypography.titleMedium)
            Text("bodyMedium - Основной текст", style = PlanetChildAppTypography.bodyMedium)
            Text("bodySmall - Мелкий текст", style = PlanetChildAppTypography.bodySmall)
            Text("labelLarge - Кнопки", style = PlanetChildAppTypography.labelLarge)
        }
    }
}