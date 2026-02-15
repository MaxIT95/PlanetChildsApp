package com.example.planetchildsapp.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.R
import com.example.planetchildsapp.client.apis.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val avatarUri: Int = R.drawable.account_circle,
    val name: String = "",
    val email: String = "",
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userApi: UserApi
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    // получить данные,
    //заполнить имя почту
    // аватар выбираем в меню профиль
    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            val userInfo = userApi.getCurrentUser();

            if (userInfo.isSuccessful) {
                val body = userInfo.body()

                Log.i("+++", "Результат огонь")
                Log.i("+++", "$body")
                if (body != null &&
                    body.name != null &&
                    body.email != null
                ) {
                    _profileState.value =
                        _profileState.value.copy(name = body.name, email = body.email)
                }
            }
        }
    }
}