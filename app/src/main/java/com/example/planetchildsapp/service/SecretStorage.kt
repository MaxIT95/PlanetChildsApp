package com.example.planetchildsapp.service

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit
import com.example.planetchildsapp.client.model.request.AuthUserRequest
import com.example.planetchildsapp.data.model.UserStorageInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SecretStorage(context: Context) {

    private val sharedPrefsName = "secure_prefs"

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        sharedPrefsName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USERS = "users"
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    fun saveUser(user: UserStorageInfo) {

        prefs.edit {
            try {
                // получаем список юзеров
                val users = getUsers().toMutableSet()
                users.add(user)
                // переводим в json
                val gson = Gson()
                val usersAsString = gson.toJson(users)
                Log.i("+++", "Инфо о юзере: $usersAsString")
                putString(KEY_USERS, usersAsString)
                Log.i("+++", "Информация для авторизации ${user.login} сохранена успешно")
            } catch (ex: Exception) {
                Log.i("+++", "Ошибка при сохранении данных пользователя: ${ex.message}")
            }
        }
    }

    fun getUsers(): List<UserStorageInfo> {

        return try {
            val gson = Gson()
            val usersAsString = prefs.getString(KEY_USERS, "[]") ?: "[]"
            Log.i("+++", "Строка информации о юзерах: $usersAsString")
            val type = object : TypeToken<List<UserStorageInfo>>() {}.type
            gson.fromJson(usersAsString, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getUserById(id: String): UserStorageInfo {
        val users = getUsers()
        return users.find { user -> user.id == id }
            ?: throw RuntimeException("Пользователь с id=$id не найден!")
    }

    fun updateUserLoginById(id: String, login: String) {
        val user = getUserById(id)
        user.login = login


        prefs.edit {
            try {
                // получаем список юзеров
                val users = getUsers().toMutableSet()
                val currentUser = users.find { it.id == id }
                    ?: throw RuntimeException("Пользователь с id=$id не найден!")
                currentUser?.login = login
                users.add(user)
                // переводим в json
                val gson = Gson()
                val usersAsString = gson.toJson(users)
                putString(KEY_USERS, usersAsString)
                Log.i("+++", "Информация для авторизации ${user.login} сохранена успешно")
            } catch (ex: Exception) {
                Log.i("+++", "Ошибка при сохранении данных пользователя: ${ex.message}")
            }
        }
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun clearTokens() {
        prefs.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
        }
    }
}