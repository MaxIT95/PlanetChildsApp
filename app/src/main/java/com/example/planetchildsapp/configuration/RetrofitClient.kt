package com.example.planetchildsapp.configuration

import com.example.planetchildsapp.service.TokenStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val DEFAULT_URL = "http://45.153.71.197:8081/"

class RetrofitClient(private val tokenStorage: TokenStorage) {

    val retrofit: Retrofit = buildRetrofit()

    private fun buildRetrofit(): Retrofit {
        // 1. Создаем логирующий интерсептор
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2. Создаем AuthInterceptor с правильным провайдером токена
        val authInterceptor = AuthInterceptor {
            tokenStorage.getAccessToken()
        }

        // 3. Создаем OkHttpClient и добавляем туда интерсепторы
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

        // 4. Настраиваем Retrofit с этим клиентом
        return Retrofit.Builder()
            .baseUrl(DEFAULT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
