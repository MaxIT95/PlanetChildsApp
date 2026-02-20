package com.example.planetchildsapp.configuration

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
        
        // Не добавляем токен к запросам авторизации и регистрации
        if (url.contains("/login") || url.contains("/register")
            || url.contains("/refresh")) {
            return chain.proceed(originalRequest)
        }
        
        val token = tokenProvider()
        val newRequest = originalRequest.newBuilder()
            .apply {
                if (!token.isNullOrEmpty()) {
                    header("Authorization", "Bearer $token")
                }
            }
            .build()
        return chain.proceed(newRequest)
    }
}