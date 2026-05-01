package com.example.planetchildsapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File

object FileUtils {

    fun createMultipartFromFile(file: File, partName: String): MultipartBody.Part {
        Log.i("++++", "${file.length()}")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
    
    fun createMultipartFromDrawable(
        context: Context,
        drawableId: Int,
        partName: String = "picture",
        fileName: String = "avatar.png",
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        quality: Int = 80
    ): MultipartBody.Part {

        // 1. Получаем Drawable по ID
        val drawable: Drawable = context.getDrawable(drawableId)
            ?: throw IllegalArgumentException("Drawable with ID $drawableId not found")

        // 2. Конвертируем в Bitmap
        val bitmap: Bitmap = when (drawable) {
            is BitmapDrawable -> drawable.bitmap
            else -> drawable.toBitmap()
        }

        // 3. Кодируем Bitmap в байт‑массив
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(compressFormat, quality, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()

        // Закрываем поток
        byteArrayOutputStream.close()

        // 4. Создаём RequestBody из байт
        val requestBody: RequestBody = RequestBody.create(
            "image/*".toMediaTypeOrNull(),
            imageBytes
        )

        // 5. Создаём MultipartBody.Part
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }

    fun createMultipartFromFileWithCompression(
        file: File,
        partName: String = "picture",
        context: Context
    ): MultipartBody.Part {

        // Проверяем существование файла
        if (!file.exists()) throw IllegalArgumentException("Файл не найден")
        if (file.isDirectory) throw IllegalArgumentException("Это директория")

        // Загружаем Bitmap из файла
        var bitmap = BitmapFactory.decodeFile(file.absolutePath)

        // Итеративно сжимаем, пока не уложимся в лимит
        var quality = 90
        var compressedBytes: ByteArray

        do {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            compressedBytes = byteArrayOutputStream.toByteArray()
            byteArrayOutputStream.close()

            // Если размер подходит или качество упало до минимума — выходим
            if (compressedBytes.size <= 500000) break
            else Log.i("+++", "Петушня ${compressedBytes.size}")

            quality -= 1 // Уменьшаем качество на 10 %
        } while (true)

        val file = createFileFromBytes(compressedBytes, context, "111.jpeg")

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    fun createFileFromBytes(
        bytes: ByteArray,
        context: Context,
        fileName: String
    ): File {

        // Получаем директорию в зависимости от типа
        val directory = context.cacheDir

        // Создаём директорию, если не существует
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Создаём файл и записываем байты
        val file = File(directory, fileName)
        file.writeBytes(bytes)

        return file
    }
}