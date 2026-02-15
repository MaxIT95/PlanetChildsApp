package com.example.planetchildsapp.service

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

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
        private const val LOGIN = "login"
        private const val PASSWORD = "password"
    }

    fun saveLogin(login: String) {
        prefs.edit {
            putString(LOGIN, login)
        }
    }

    fun savePassword(password: String) {
        prefs.edit {
            putString(PASSWORD, password)
        }
    }

    fun getLogin(): String? {
        return prefs.getString(LOGIN, null)
    }

    fun getPassword(): String? {
        return prefs.getString(PASSWORD, null)
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun clearTokens() {
        prefs.edit { clear() }
    }
}