package com.vighn.chat_application.presentation.Auth_Screen

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vighn.chat_application.Auth.AuthService
import com.vighn.chat_application.Auth.models.AuthRequest
import com.vighn.chat_application.Auth.models.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _authState = mutableStateOf(AuthState())
    val authState : State<AuthState> = _authState

    private val _authResult = MutableSharedFlow<AuthResult<Unit>>()
    val authResult : SharedFlow<AuthResult<Unit>> = _authResult

    var username = preferences.getString("username",null)

    init {
        authenticate()
    }

    fun onEvent(event : AuthEvents){
        when (event){
            is AuthEvents.signIN_UsernameChanged ->{
                _authState.value = authState.value.copy(
                    signInUsername = event.value
                )
            }
            is AuthEvents.signIN_PasswordChanged ->{
                _authState.value = authState.value.copy(
                    signInPassword = event.value
                )
            }
            is AuthEvents.signIN_ButtonPressed ->{
                signIN()
            }
            is AuthEvents.signUP_UsernameChanged ->{
                _authState.value = authState.value.copy(
                    signUpUsername = event.value
                )
            }
            is AuthEvents.signUP_PasswordChanged ->{
                _authState.value = authState.value.copy(
                    signUpPassword = event.value
                )
            }
            is AuthEvents.signUP_ButtonPressed ->{
                signUP()
            }
        }
    }

    fun signIN() {
        viewModelScope.launch {
            _authState.value=authState.value.copy(
                isLoading = true
            )
            authService.signIN(
                AuthRequest(
                    username = authState.value.signInUsername,
                    password = authState.value.signInPassword
                )

            )
            _authState.value=authState.value.copy(
                isLoading = false
            )
            authenticate()
        }
    }
    fun signUP() {
        viewModelScope.launch {
            _authState.value=authState.value.copy(
                isLoading = true
            )
            authService.signUP(
                AuthRequest(
                    username = authState.value.signUpUsername,
                    password = authState.value.signUpPassword
                )

            )
            _authState.value=authState.value.copy(
                isLoading = false
            )
        }
    }
    fun authenticate(){
        viewModelScope.launch {
            println("inside viewmodel authenticate")
            _authState.value=authState.value.copy(
                isLoading = true
            )
            username = preferences.getString("username",null)
            println(username)
            val result = authService.authenticate()
            _authResult.emit(result)
            _authState.value=authState.value.copy(
                isLoading = false
            )

        }
    }
}