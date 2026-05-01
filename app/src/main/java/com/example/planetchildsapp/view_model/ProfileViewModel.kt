package com.example.planetchildsapp.view_model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.client.model.request.UserProfileRequest
import com.example.planetchildsapp.data.repository.UserPrefsRepository
import com.example.planetchildsapp.service.SecretStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class ProfileState(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val avatarPath: String? = null,
    val isNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true
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
        observeAvatar()
        loadUserInfo()
    }

    fun clearSuccessStatus() {
        _profileState.value = _profileState.value.copy(isSuccess = false)
    }

    /**
     * Реактивная подписка на аватар из DataStore.
     * Обновляет UI сразу при любом изменении — не нужно ждать одноразового getUserPrefs().
     */
    private fun observeAvatar() {
        viewModelScope.launch {
            userPrefsRepository.avatarPathFlow.collect { avatarPath ->
                _profileState.update { it.copy(avatarPath = avatarPath) }
            }
        }
    }

    /**
     * Метод для сохранения изменений
     */
    fun onSaveChanges() {
        if (_profileState.value.isEmailValid &&
            _profileState.value.isPasswordValid &&
            _profileState.value.isNameValid
        ) {
            viewModelScope.launch {
                val userRequest = UserProfileRequest(
                    email = _profileState.value.email,
                    name = _profileState.value.name,
                )
                //вызываем метод api backend для обновления
                val updateResponse =
                    userApi.updateUser(_profileState.value.id.toLong(), userRequest)

                val responseBody = updateResponse.body()

                if (updateResponse.raw().isSuccessful &&
                    responseBody != null
                ) {
                    Log.i("+++", "Запрос успешно выполнен")
                    //уведомление что все ок
                    _profileState.value = _profileState.value.copy(
                        name = responseBody.name ?: "",
                        email = responseBody.email ?: "",
                        isLoading = false,
                        isSuccess = true
                    )
                    // обновляем data store, secret
                    userPrefsRepository.saveEmail(responseBody.email ?: "")
                    userPrefsRepository.saveName(responseBody.name ?: "")
                    Log.i("+++", "userPrefsRepository updated complete")
                    secretStorage.saveLogin(responseBody.email ?: "")
                }
            }
        }
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
                val password = secretStorage.getPassword()

                if (cachedData.id != null &&
                    cachedData.name != null &&
                    cachedData.email != null &&
                    password != null
                ) {
                    // Данные уже есть в кэше
                    // avatarUri не трогаем — им управляет observeAvatar()
                    Log.i("+++", "Загружены данные из DataStore: $cachedData")
                    _profileState.value = _profileState.value.copy(
                        id = cachedData.id,
                        password = password,
                        name = cachedData.name,
                        email = cachedData.email,
                        isLoading = false,
                    )
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

    fun onPasswordChange(newPassword: String) {
        _profileState.update {
            it.copy(
                password = newPassword,
                isPasswordValid = newPassword.isNotEmpty() && newPassword.length > 7
            )
        }
    }

    fun onNameChange(name: String) {
        _profileState.update {
            it.copy(name = name, isNameValid = name.isNotEmpty())
        }
    }

    fun onEmailChange(email: String) {
        _profileState.update {
            it.copy(email = email, isEmailValid = email.isNotEmpty())
        }
    }

    fun isPasswordValid(): Boolean {
        return _profileState.value.password.length >= 8
    }

    // метод для сохранения во внутреннее хранилище аватарки
    fun copyPhotoPickerUriToInternalStorage(uri: Uri) {
        // Создаём имя файла, например, с временной меткой
        val filename = "avatar_${System.currentTimeMillis()}.jpg"
        val destination = File(context.filesDir, filename)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            destination.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        _profileState.value = _profileState.value.copy(avatarPath = destination.absolutePath)
        viewModelScope.launch {
            userPrefsRepository.saveAvatarPath(destination.absolutePath)
        }
    }

    /**
     * Выход из аккаунта - очистка данных
     */
    fun logout() {
        viewModelScope.launch {
            secretStorage.clearTokens()
            userPrefsRepository.clearUserPrefs()
            _profileState.value = ProfileState(avatarPath = null) // Сброс в дефолтное состояние
            Log.i("+++", "Пользователь вышел из аккаунта")
        }
    }
}
