package com.vighn.chat_application.presentation.Auth_Screen

data class AuthState(
    val isLoading : Boolean =false,
    val signUpUsername: String = "",
    val signUpPassword: String = "",
    val signInUsername: String = "",
    val signInPassword: String = ""
)
