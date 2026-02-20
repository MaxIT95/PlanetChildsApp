package com.example.planetchildsapp.view_model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.R
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.data.repository.UserPrefsRepository
import com.example.planetchildsapp.service.SecretStorage
import com.example.planetchildsapp.utils.saveImageToInternalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val avatarUri: Int = R.drawable.account_circle,
    val avatarPath: String? = null, // Путь к файлу аватара в internal storage
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userApi: UserApi,
    private val secretStorage: SecretStorage,
    private val userPrefsRepository: UserPrefsRepository,
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadUserInfo()
        loadAvatar()
    }

    /**
     * Загрузка информации о пользователе
     * 1. Проверяем DataStore - если данные есть, используем их
     * 2. Если данных нет - делаем HTTP запрос на /me
     * 3. Сохраняем полученные данные в DataStore
     */
    fun loadUserInfo() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true, error = null)

            try {
                // Шаг 1: Проверяем данные в DataStore
                val cachedData = userPrefsRepository.getUserPrefs()

                if (cachedData.name != null && cachedData.email != null) {
                    // Данные уже есть в кэше
                    Log.i("ProfileViewModel", "Загружены данные из DataStore: $cachedData")
                    _profileState.value = _profileState.value.copy(
                        name = cachedData.name,
                        email = cachedData.email,
                        isLoading = false
                    )
//                } else {
////                    // Шаг 2: Данных нет - загружаем с сервера
////                    Log.i("ProfileViewModel", "Данных в DataStore нет, загружаем с сервера...")
////                    fetchUserFromApi()
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка загрузки данных", e)
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки данных: ${e.message}"
                )
            }
        }
    }

    /**
     * Загрузка аватара из DataStore
     */
    private fun loadAvatar() {
        viewModelScope.launch {
            userPrefsRepository.avatarUrlFlow.collect { avatarPath ->
                _profileState.value = _profileState.value.copy(avatarPath = avatarPath)
            }
        }
    }

    /**
     * Обработка выбранного аватара из галереи
     */
    fun onAvatarSelected(uri: Uri) {
        viewModelScope.launch {
            try {
                // Сохраняем изображение в internal storage
                val savedPath = context.saveImageToInternalStorage(uri)

                if (savedPath != null) {
                    // Сохраняем путь в DataStore
                    userPrefsRepository.saveAvatarUrl(savedPath)

                    // Обновляем UI
                    _profileState.value = _profileState.value.copy(avatarPath = savedPath)

                    Log.i("ProfileViewModel", "Аватар сохранен: $savedPath")
                } else {
                    Log.e("ProfileViewModel", "Ошибка сохранения аватара")
                    _profileState.value = _profileState.value.copy(
                        error = "Не удалось сохранить изображение"
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка обработки аватара", e)
                _profileState.value = _profileState.value.copy(
                    error = "Ошибка: ${e.message}"
                )
            }
        }
    }

    /**
     * Загрузка данных пользователя с сервера
     */
//    private suspend fun fetchUserFromApi() {
//        try {
//            val response = userApi.getCurrentUser()
//
//            if (response.isSuccessful) {
//                val userInfo = response.body()
//
//                if (userInfo != null && userInfo.name != null && userInfo.email != null) {
//                    Log.i("ProfileViewModel", "Получены данные с сервера: $userInfo")
//
//                    // Сохраняем в DataStore для будущих запусков
//                    userPrefsRepository.saveUserPrefs(
//                        name = userInfo.name,
//                        email = userInfo.email,
//                        role = userInfo.role,
//                        avatarUrl = null // TODO: добавить поддержку avatar URL из API
//                    )
//
//                    // Обновляем UI
//                    _profileState.value = _profileState.value.copy(
//                        name = userInfo.name,
//                        email = userInfo.email,
//                        isLoading = false
//                    )
//                } else {
//                    Log.w("ProfileViewModel", "Получены некорректные данные с сервера")
//                    _profileState.value = _profileState.value.copy(
//                        isLoading = false,
//                        error = "Получены неполные данные с сервера"
//                    )
//                }
//            } else {
//                Log.e("ProfileViewModel", "Ошибка HTTP: ${response.code()}")
//                _profileState.value = _profileState.value.copy(
//                    isLoading = false,
//                    error = "Ошибка сервера: ${response.code()}"
//                )
//            }
//        } catch (e: Exception) {
//            Log.e("ProfileViewModel", "Ошибка сетевого запроса", e)
//            _profileState.value = _profileState.value.copy(
//                isLoading = false,
//                error = "Ошибка сети: ${e.message}"
//            )
//        }
//    }

    /**
     * Принудительная перезагрузка данных с сервера
     */
//    fun refreshUserInfo() {
//        viewModelScope.launch {
//            _profileState.value = _profileState.value.copy(isLoading = true, error = null)
//            fetchUserFromApi()
//        }
//    }

    /**
     * Выход из аккаунта - очистка данных
     */
    fun logout() {
        viewModelScope.launch {
            secretStorage.clearTokens()
            userPrefsRepository.clearUserPrefs()
            _profileState.value = ProfileState() // Сброс в дефолтное состояние
            Log.i("ProfileViewModel", "Пользователь вышел из аккаунта")
        }
    }
}
