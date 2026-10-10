package com.example.planetchildsapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.client.model.enums.Role
import com.example.planetchildsapp.client.model.request.AuthUserRequest
import com.example.planetchildsapp.client.model.request.UserRegistrationRequest
import com.example.planetchildsapp.data.model.UserStorageInfo
import com.example.planetchildsapp.data.model.parseToken
import com.example.planetchildsapp.data.repository.UserPrefsRepository
import com.example.planetchildsapp.service.SecretStorage
import com.example.planetchildsapp.utils.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegistrationState(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val isLoading: Boolean = false,
//    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

data class RegistrationErrors(
    val isNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val errorMessage: String? = null,
    val isRepeatPasswordValid: Boolean = true
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val secretStorage: SecretStorage,
    private val userApi: UserApi,
    private val userPrefsRepository: UserPrefsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationState())
    val uiState: StateFlow<RegistrationState> = _uiState.asStateFlow()

    private val _errorsUuiState = MutableStateFlow(RegistrationErrors())
    val errorsUiState: StateFlow<RegistrationErrors> = _errorsUuiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update {
            it.copy(name = newName)
        }
        _errorsUuiState.update {
            it.copy(
                isNameValid = newName.isNotEmpty(),
                errorMessage = null
            )
        }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
        _errorsUuiState.update {
            it.copy(
                isEmailValid = newEmail.isNotEmpty(),
                errorMessage = null
            )
        }
    }

    fun onRepeatPasswordChange(newEmail: String) {
        _uiState.update { it.copy(repeatPassword = newEmail) }
        _errorsUuiState.update {
            it.copy(
                isRepeatPasswordValid = _uiState.value.password == newEmail,
                errorMessage = null
            )
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        _errorsUuiState.update {
            it.copy(
                isPasswordValid = newPassword.isNotEmpty(),
                errorMessage = null
            )
        }
    }

    fun register(selectedRole: UserRole) {
        val state = _uiState.value
        val registrationRole = when (selectedRole) {
            UserRole.Parent -> Role.PARENT
            UserRole.Coach -> Role.COACH
        }

        _errorsUuiState.update {
            it.copy(
                isNameValid = state.name.isNotEmpty(),
                isEmailValid = state.email.isNotEmpty(),
                isPasswordValid = state.password.isNotEmpty(),
                isRepeatPasswordValid = state.repeatPassword.isNotEmpty() &&
                        state.repeatPassword == state.password
            )
        }
        Log.i("+++", "Начало регистрации пользователя...")
        // Валидация
        if (errorsUiState.value.isNameValid &&
            errorsUiState.value.isEmailValid &&
            errorsUiState.value.isRepeatPasswordValid &&
            errorsUiState.value.isPasswordValid
        ) {
            viewModelScope.launch {
                try {
                    _uiState.update { it.copy(isLoading = true) }

                    val response = userApi.registerUser(
                        UserRegistrationRequest(
                            role = registrationRole,
                            lastName = " ",
                            firstName = state.name,
                            middleName = " ",
                            email = state.email,
                            password = state.password
                        )
                    )

                    if (response.isSuccessful && response.body() != null) {
                        // кладем логин и пароль в секреты
                        val userStorage = UserStorageInfo(
                            response.body()?.id.toString(),
                            state.email, state.password
                        )
                        secretStorage.saveUser(userStorage)

                        val authResponse = userApi.loginUser(
                            AuthUserRequest(
                                email = state.email,
                                password = state.password
                            )
                        )
                        if (authResponse.isSuccessful) {
                            val authBody = authResponse.body()
                            if (authBody != null) {
                                secretStorage.saveTokens(
                                    authBody.accessToken,
                                    authBody.refreshToken
                                )
                                //парсим токен и кладем имя почту и роль в datastore
                                val token = parseToken(authBody.accessToken)
                                // сохраняем в дата стор
                                userPrefsRepository.saveUserPrefs(
                                    token.id, token.name.trim(),
                                    token.email, token.role,
                                    null
                                )
                            }
                        } else {
                            Log.e(
                                "+++", "Ошибка авторизации  ${authResponse.code()}" +
                                        " ${authResponse.message()}"
                            )
                            _errorsUuiState.update { it.copy(errorMessage = "Ошибка авторизации ${authResponse.message()}") }
                        }
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    } else {
                        Log.i("+++", "ошибка регистрации: ${response.body()}, $response")
                        _errorsUuiState.update { it.copy(errorMessage = "Ошибка регистрации ${response.message()}") }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }
                } catch (exception: Exception) {
                    Log.e("***", "Что случилось $exception")
                }
            }
        } else {
            _errorsUuiState.update { it.copy(errorMessage = "Пожалуйста, заполните все поля корректно") }
            return
        }
    }
}
