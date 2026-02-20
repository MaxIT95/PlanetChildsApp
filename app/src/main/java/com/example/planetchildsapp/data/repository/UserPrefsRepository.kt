package com.example.planetchildsapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class UserPrefsData(
    val name: String? = null,
    val email: String? = null,
    val role: String? = null,
    val avatarUrl: String? = null
)

@Singleton
class UserPrefsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val AVATAR_URL_KEY = stringPreferencesKey("avatar_url")
    }

    // Flow для реактивного получения всех данных сразу
    val userPrefsFlow: Flow<UserPrefsData> = dataStore.data
        .map { preferences ->
            UserPrefsData(
                name = preferences[NAME_KEY],
                email = preferences[EMAIL_KEY],
                role = preferences[ROLE_KEY],
                avatarUrl = preferences[AVATAR_URL_KEY]
            )
        }

    // Отдельные Flow на случай, если нужны по отдельности
    val nameFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[NAME_KEY]
        }

    val emailFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[EMAIL_KEY]
        }

    val roleFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[ROLE_KEY]
        }

    val avatarUrlFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[AVATAR_URL_KEY]
        }

    // Метод для единовременного получения данных (не Flow)
    suspend fun getUserPrefs(): UserPrefsData {
        return userPrefsFlow.first()
    }

    // Сохранить все данные одной транзакцией
    suspend fun saveUserPrefs(name: String, email: String, role: String, avatarUrl: String?) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[EMAIL_KEY] = email
            preferences[ROLE_KEY] = role
            if (avatarUrl != null) {
                preferences[AVATAR_URL_KEY] = avatarUrl
            }
        }
    }

    // Отдельные методы для сохранения (если нужно обновлять по одному полю)
    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun saveRole(role: String) {
        dataStore.edit { preferences ->
            preferences[ROLE_KEY] = role
        }
    }

    suspend fun saveAvatarUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[AVATAR_URL_KEY] = url
        }
    }

    // Очистить все данные пользователя (при logout)
    suspend fun clearUserPrefs() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(ROLE_KEY)
            preferences.remove(AVATAR_URL_KEY)
        }
    }
}
