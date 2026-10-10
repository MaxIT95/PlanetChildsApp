package com.example.planetchildsapp.configuration

import android.util.Log
import com.example.planetchildsapp.client.model.request.RefreshTokenRequest
import com.example.planetchildsapp.client.model.response.RefreshTokenResponse
import com.example.planetchildsapp.service.SecretStorage
import com.google.gson.Gson
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route

private const val TAG = "+++1"

class TokenAuthenticator(
    private val tokenStorage: SecretStorage
) : Authenticator {

    private val gson = Gson()

    // Синхронизируем, чтобы при параллельных 401 только один поток делал refresh
    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        // Токен, который был в запросе, вызвавшем 401
        val tokenUsedInRequest = response.request.header("Authorization")
            ?.removePrefix("Bearer ")

        // Если хранимый токен уже отличается — другой поток успел обновить.
        // Просто повторяем запрос с актуальным токеном.
        val currentStoredToken = tokenStorage.getAccessToken()
        if (currentStoredToken != null && currentStoredToken != tokenUsedInRequest) {
            return response.request.newBuilder()
                .header("Authorization", "Bearer $currentStoredToken")
                .build()
        }

        val refreshToken = tokenStorage.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            Log.w(TAG, "No refresh token available, cannot refresh")
            return null
        }

        Log.d(TAG, "Access token expired, attempting refresh")

        val body = gson.toJson(RefreshTokenRequest(refreshToken))
            .toRequestBody("application/json".toMediaType())

        val refreshRequest = Request.Builder()
            .url("${DEFAULT_URL}api/user/refresh")
            .post(body)
            .build()

        // Отдельный клиент без аутентификатора — избегаем рекурсии
        val refreshResponse = try {
            OkHttpClient().newCall(refreshRequest).execute()
        } catch (e: Exception) {
            Log.e(TAG, "Refresh request failed: ${e.message}")
            return null
        }

        if (!refreshResponse.isSuccessful) {
            Log.w(TAG, "Refresh token rejected (${refreshResponse.code}), clearing tokens")
            tokenStorage.clearTokens()
            return null
        }

        val responseBody = refreshResponse.body?.string()
        if (responseBody.isNullOrEmpty()) {
            Log.e(TAG, "Empty refresh response body")
            return null
        }

        val tokens = try {
            gson.fromJson(responseBody, RefreshTokenResponse::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse refresh response: ${e.message}")
            return null
        }

        tokenStorage.saveTokens(tokens.accessToken, tokens.refreshToken)
        Log.d(TAG, "Token refreshed successfully")

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${tokens.accessToken}")
            .build()
    }
}
