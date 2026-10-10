package com.example.planetchildsapp.view_model

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.client.model.request.AuthUserRequest
import com.example.planetchildsapp.client.model.request.RefreshTokenRequest
import com.example.planetchildsapp.data.model.UserStorageInfo
import com.example.planetchildsapp.data.model.parseToken
import com.example.planetchildsapp.data.repository.UserPrefsRepository
import com.example.planetchildsapp.service.SecretStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthorizationState(
    val login: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

data class AuthorizationErrors(
    val isLoginValid: Boolean = true,
    val isPasswordValid: Boolean = true
)

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val secretStorage: SecretStorage,
    private val userApi: UserApi,
    private val userPrefsRepository: UserPrefsRepository
) : ViewModel() {

    private val _usersInfoState: MutableStateFlow<List<UserStorageInfo>> =
        MutableStateFlow(emptyList())
    val usersInfoState: StateFlow<List<UserStorageInfo>> = _usersInfoState.asStateFlow()

    // Состояние выпадающего списка
    private var _showDropdownState = MutableStateFlow(false)
    var showDropdownState = _showDropdownState.asStateFlow()

    private val _uiState = MutableStateFlow(AuthorizationState())
    private val _errorsUuiState = MutableStateFlow(AuthorizationErrors())
    val uiState: StateFlow<AuthorizationState> = _uiState.asStateFlow()
    val errorsUiState: StateFlow<AuthorizationErrors> = _errorsUuiState.asStateFlow()
    private val _resultAuthorize = MutableStateFlow(false)
    private val _isAuthChecking = MutableStateFlow(true)
    val resultAuth: StateFlow<Boolean> = _resultAuthorize
    val isAuthChecking: StateFlow<Boolean> = _isAuthChecking.asStateFlow()

    init {
        //загружаем юзеров
        viewModelScope.launch {
            _usersInfoState.value = secretStorage.getUsers();
        }
    }

    /**
    метод для проверки наличия токена,
    автовхода и автоматического обновления через рефреш
     */
    fun checkActualToken() {
        viewModelScope.launch {
            _isAuthChecking.value = true

            try {
                Log.i("+++", "Проверяю наличие токена...")
                val accessToken = secretStorage.getAccessToken()

                if (accessToken.isNullOrEmpty()) {
                    _resultAuthorize.value = false
                    return@launch
                }

                Log.i("+++", "Ура, токен есть, но надо проверить его актуальность")
                Log.i("+++", accessToken)

                val currentUser = userApi.getCurrentUser()

                if (currentUser.isSuccessful) {
                    Log.i("+++", "Отлично, токен актуальный")

                    val token = parseToken(accessToken)
                    val savedAvatar = userPrefsRepository.getUserPrefs().avatarPath
                    val base64 = currentUser.body()?.profile?.contents?.firstOrNull()?.data
                    val avatarToSave = if (!base64.isNullOrBlank()) {
                        saveAvatarFromBase64(token.id, base64) ?: savedAvatar
                    } else {
                        savedAvatar
                    }
                    userPrefsRepository.saveUserPrefs(
                        token.id,
                        token.name.trim(),
                        token.email,
                        token.role,
                        avatarToSave
                    )
                    _resultAuthorize.value = true
                    return@launch
                }

                Log.e("+++", "Токен просрочен...")
                val refreshToken = secretStorage.getRefreshToken()
                Log.i("+++", "refresh token: $refreshToken")

                if (refreshToken.isNullOrEmpty()) {
                    _resultAuthorize.value = false
                    return@launch
                }

                val refreshResponse = userApi.refreshToken(
                    request = RefreshTokenRequest(refreshToken)
                )

                if (refreshResponse.isSuccessful) {
                    Log.i("+++", "Токен успешно обновлен!")

                    val body = refreshResponse.body() ?: throw RuntimeException()
                    secretStorage.saveTokens(body.accessToken, body.refreshToken)

                    val token = parseToken(body.accessToken)
                    val savedAvatar = userPrefsRepository.getUserPrefs().avatarPath
                    userPrefsRepository.saveUserPrefs(
                        token.id,
                        token.name.trim(),
                        token.email,
                        token.role,
                        savedAvatar
                    )
                    _resultAuthorize.value = true
                } else {
                    Log.e("+++", "refreshToken не сработал...$refreshToken")
                    Log.e("+++", "${refreshResponse.errorBody()?.string()}")
                    _resultAuthorize.value = false
                }
            } catch (e: Exception) {
                Log.e("+++", "Ошибка проверки токена", e)
                _resultAuthorize.value = false
            } finally {
                _isAuthChecking.value = false
            }
        }
    }

    fun onLoginChange(login: String) {
        _uiState.update { it.copy(login = login) }
    }

    fun onLoginFocusChanged(isFocused: Boolean) {
        val hasStoredUsers = _usersInfoState.value.isNotEmpty()
        _showDropdownState.value = isFocused && hasStoredUsers
    }

    fun hideDropdown() {
        _showDropdownState.value = false
    }

    fun onLoginSuggestionSelected(user: UserStorageInfo) {
        _uiState.update {
            it.copy(login = user.login, password = user.password)
        }
        _errorsUuiState.update {
            it.copy(isLoginValid = true, isPasswordValid = true)
        }
        _showDropdownState.value = false
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(password = newPassword)
        }
    }

    fun clearUiState() {
        _uiState.update {
            it.copy(
                login = "",
                password = "", isSuccess = false
            )
        }
    }

    fun authorize() {
        viewModelScope.launch {
            val state = _uiState.value

            _errorsUuiState.update {
                it.copy(
                    isLoginValid = state.login.isNotEmpty(),
                    isPasswordValid = state.password.isNotEmpty() && state.password.length >= 8
                )
            }

            if (_errorsUuiState.value.isLoginValid && _errorsUuiState.value.isPasswordValid) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val response = userApi.loginUser(
                    AuthUserRequest(state.login, state.password)
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        secretStorage.saveTokens(body.accessToken, body.refreshToken)
                        // парсим токен и кладем имя почту и роль в datastore
                        val token = parseToken(body.accessToken)
                        // сохраняем в дата стор
                        val savedAvatar = userPrefsRepository.getUserPrefs().avatarPath
                        val avatarToSave = body.url ?: savedAvatar
                        userPrefsRepository.saveUserPrefs(
                            token.id,
                            token.name.trim(), token.email,
                            token.role, avatarToSave
                        )
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                        // ключеовй момент - сохраняем объект юзера в
                        // секретное хранилище в виде списка юхзеров
                        // для последующего выбора из выпад. меню при авторизации
                        val storedUser = UserStorageInfo(
                            token.id,
                            state.login,
                            state.password
                        )
                        secretStorage.saveUser(storedUser)
                        _usersInfoState.update { users ->
                            listOf(storedUser) + users.filterNot { it.login == storedUser.login }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Ошибка авторизации. Попробуйте снова."
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Пользователь не зарегистрирован"
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }

    /**
     * Декодирует base64-строку аватара с сервера и сохраняет в локальный файл.
     * @return абсолютный путь к файлу или null при ошибке
     */
    private fun saveAvatarFromBase64(userId: String, base64Data: String): String? {
        return try {
            val normalized = base64Data.substringAfter("base64,", base64Data)
            val bytes = Base64.decode(normalized, Base64.DEFAULT)
            val filename = "avatar_${userId}.jpg"
            val file = File(context.filesDir, filename)
            file.writeBytes(bytes)
            Log.i("+++", "Аватар сохранён локально: ${file.absolutePath}")
            file.absolutePath
        } catch (e: Exception) {
            Log.e("AuthorizationVM", "Ошибка сохранения аватара из base64", e)
            null
        }
    }
}