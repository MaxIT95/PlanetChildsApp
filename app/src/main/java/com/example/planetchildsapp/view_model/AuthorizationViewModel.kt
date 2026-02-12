package com.example.planetchildsapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.client.apis.UserApi
import com.example.planetchildsapp.client.model.request.AuthUserRequest
import com.example.planetchildsapp.configuration.RetrofitClient
import com.example.planetchildsapp.service.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthorizationState(
    val login: String = "user@example.com",
    val password: String = "SecurePass123!",
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
    private val tokenStorage: TokenStorage,
    private val userApi: UserApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorizationState())
    private val _errorsUuiState = MutableStateFlow(AuthorizationErrors())
    val uiState: StateFlow<AuthorizationState> = _uiState.asStateFlow()
    val errorsUiState: StateFlow<AuthorizationErrors> = _errorsUuiState.asStateFlow()

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

        Log.i("+++", "go")
        viewModelScope.launch {
            val state = _uiState.value

            _errorsUuiState.update {
                it.copy(
                    isLoginValid = state.login.isNotEmpty(),
                    isPasswordValid = state.password.isNotEmpty() && state.password.length >= 8
                )
            }
            Log.i("+++", "go222")

            if (_errorsUuiState.value.isLoginValid && _errorsUuiState.value.isPasswordValid) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val response = userApi.loginUser(
                    AuthUserRequest(state.login, state.password)
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        tokenStorage.saveTokens(body.accessToken, body.refreshToken)
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "РџСѓСЃС‚РѕР№ РѕС‚РІРµС‚ РѕС‚ СЃРµСЂРІРµСЂР°"
                            )
                        }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "РћС€РёР±РєР° СЃРµС‚Рё") }
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "РћС€РёР±РєР° Р°РІС‚РѕСЂРёР·Р°С†РёРё")
                }
            }
        }
    }
}