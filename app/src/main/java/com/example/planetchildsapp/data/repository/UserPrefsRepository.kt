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
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val role: String? = null,
    val avatarPath: String? = null
)

@Singleton
class UserPrefsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val AVATAR_PATH_KEY = stringPreferencesKey("avatar_path")
    }

    // Flow для реактивного получения всех данных сразу
    val userPrefsFlow: Flow<UserPrefsData> = dataStore.data
        .map { preferences ->
            UserPrefsData(
                id = preferences[ID_KEY],
                name = preferences[NAME_KEY],
                email = preferences[EMAIL_KEY],
                role = preferences[ROLE_KEY],
                avatarPath = preferences[AVATAR_PATH_KEY]
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

    val idFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[ID_KEY]
        }

    val avatarPathFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[AVATAR_PATH_KEY]
        }

    // Метод для единовременного получения данных (не Flow)
    suspend fun getUserPrefs(): UserPrefsData {
        return userPrefsFlow.first()
    }

    // Сохранить все данные одной транзакцией
    suspend fun saveUserPrefs(
        id: String,
        name: String,
        email: String,
        role: String,
        avatarUri: String?
    ) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = id
            preferences[NAME_KEY] = name
            preferences[EMAIL_KEY] = email
            preferences[ROLE_KEY] = role
            if (avatarUri != null) {
                preferences[AVATAR_PATH_KEY] = avatarUri
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

    suspend fun saveId(id: String) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = id
        }
    }

    suspend fun saveAvatarPath(filePath: String) {
        dataStore.edit { preferences ->
            preferences[AVATAR_PATH_KEY] = filePath
        }
    }

    suspend fun saveChildAvatar(fileName: String, avatarPath: String) {
        dataStore.edit { preferences ->
            val childAvatarKey = stringPreferencesKey("avatar_path_$fileName")
            preferences[childAvatarKey] = avatarPath
        }
    }

    // Очистить все данные пользователя (при logout)
    suspend fun clearUserPrefs() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(ROLE_KEY)
            preferences.remove(AVATAR_PATH_KEY)
        }
    }
}
