package com.example.planetchildsapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.client.model.enums.Role
import com.example.planetchildsapp.client.model.request.UserRegistrationRequest
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
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

data class RegistrationErrors(
    val isNameValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userApi: UserApi
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
            it.copy(isNameValid = newName.isNotEmpty())
        }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
        _errorsUuiState.update {
            it.copy(isEmailValid = newEmail.isEmpty())
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        _errorsUuiState.update {
            it.copy(isPasswordValid = newPassword.isEmpty())
        }
    }

    fun register() {
        val state = _uiState.value

        _errorsUuiState.update {
            it.copy(
                isNameValid = state.name.isNotEmpty(),
                isEmailValid = state.email.isNotEmpty(),
                isPasswordValid = state.password.isNotEmpty()
            )
        }

        // Валидация
        if (errorsUiState.value.isNameValid &&
            errorsUiState.value.isEmailValid &&
            errorsUiState.value.isPasswordValid
        ) {
            _uiState.update { it.copy(errorMessage = null) }
            viewModelScope.launch {
                try {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    // имитация запроса регистрации
                    //            delay(2000)
                    val response = userApi.registerUser(
                        UserRegistrationRequest(
                            role = Role.PARENT,
                            lastName = "Max",
                            firstName = "Max",
                            middleName = "Max",
                            email = "max@mail.ru",
                            password = "1234"
                        )
                    )
                    Log.i("***", "сделал запрос")
//                    val success = response.isSuccessful

//                    if (success) {
//                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
//                    } else {
//                        _uiState.update {
//                            it.copy(
//                                isLoading = false,
//                                errorMessage = "Ошибка регистрации"
//                            )
//                        }
//                    }
                } catch (exception: Exception) {
                    Log.e("***", "Что случилось $exception")
                }
            }
        } else {
            _uiState.update { it.copy(errorMessage = "Пожалуйста, заполните все поля корректно") }
            return
        }
    }
}
