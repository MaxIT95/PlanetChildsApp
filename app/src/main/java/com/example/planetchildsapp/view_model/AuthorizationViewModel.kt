package com.example.planetchildsapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.client.model.request.AuthUserRequest
import com.example.planetchildsapp.client.model.request.RefreshTokenRequest
import com.example.planetchildsapp.service.SecretStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    private val secretStorage: SecretStorage,
    private val userApi: UserApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(initAuthState())
    private val _errorsUuiState = MutableStateFlow(AuthorizationErrors())
    val uiState: StateFlow<AuthorizationState> = _uiState.asStateFlow()
    val errorsUiState: StateFlow<AuthorizationErrors> = _errorsUuiState.asStateFlow()
    private val _resultAuthorize = MutableStateFlow(false)
    val resultAuth: StateFlow<Boolean> = _resultAuthorize

    private fun initAuthState(): AuthorizationState {
        val login = secretStorage.getLogin()
        val password = secretStorage.getPassword()

        if (login != null && password != null) {
            return AuthorizationState(login = login, password = password)
        }
        return AuthorizationState()
    }

    /**
    метод для проверки наличия токена,
    автовхода и автоматического обновления через рефреш
     */
    fun checkActualToken() {
        Log.i("+++", "Проверяю наличие токена...")
        val token = secretStorage.getAccessToken()

        if (token != null &&
            token.isNotEmpty()
        ) {
            Log.i("+++", "Ура, токен есть, но надо проверить его актуальность")
            viewModelScope.async {
                val currentUser = userApi.getCurrentUser()

                if (currentUser.isSuccessful) {
                    Log.i("+++", "Отлично, токен актуальный")
                    _resultAuthorize.value = true
                } else {
                    Log.e("+++", "Токен просрочен...")
                    val refreshToken = secretStorage.getRefreshToken()

                    if (refreshToken != null) {
                        val refreshResponse =
                            userApi.refreshToken(
                                request = RefreshTokenRequest(refreshToken)
                            )
                        if (refreshResponse.isSuccessful) {
                            Log.i("+++", "Токен успешно обновлен!")
                            _resultAuthorize.value = true
                        } else {
                            Log.e("+++", "refreshToken не сработал...")
                            _resultAuthorize.value = false
                        }
                    } else {
                        _resultAuthorize.value = false
                    }
                }
            }
        } else {
            _resultAuthorize.value = false
        }
    }

    fun onLoginChange(login: String) {
        _uiState.update { it.copy(login = login) }
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
                        secretStorage.saveLogin(state.login)
                        secretStorage.savePassword(state.password)

                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Ерунда...°"
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
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }
}