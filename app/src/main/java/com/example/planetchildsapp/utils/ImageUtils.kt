package com.example.planetchildsapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileOutputStream

/**
 * Extension функции для работы с изображениями
 */

/**
 * Сохраняет изображение из Uri в internal storage
 * @return путь к сохраненному файлу или null при ошибке
 */
fun Context.saveImageToInternalStorage(uri: Uri): String? {
    return try {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        
        // Удаляем старый аватар, если существует
        deleteOldAvatar()
        
        // Создаем новый файл с уникальным именем
        val fileName = "avatar_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)
        
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                // Оптимизируем изображение перед сохранением
                val bitmap = BitmapFactory.decodeStream(input)
                val resizedBitmap = resizeBitmap(bitmap, maxWidth = 512, maxHeight = 512)
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)
                resizedBitmap.recycle()
                bitmap.recycle()
            }
        }
        
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Загружает изображение из файла и конвертирует в ImageBitmap для Compose
 */
fun loadImageBitmapFromPath(path: String?): ImageBitmap? {
    if (path == null) return null
    
    return try {
        val file = File(path)
        if (!file.exists()) return null
        
        val bitmap = BitmapFactory.decodeFile(path)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Удаляет старый файл аватара
 */
private fun Context.deleteOldAvatar() {
    try {
        filesDir.listFiles()?.forEach { file ->
            if (file.name.startsWith("avatar_") && file.name.endsWith(".jpg")) {
                file.delete()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Изменяет размер Bitmap для оптимизации
 */
private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    
    if (width <= maxWidth && height <= maxHeight) {
        return bitmap
    }
    
    val ratio = minOf(
        maxWidth.toFloat() / width,
        maxHeight.toFloat() / height
    )
    
    val newWidth = (width * ratio).toInt()
    val newHeight = (height * ratio).toInt()
    
    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}
