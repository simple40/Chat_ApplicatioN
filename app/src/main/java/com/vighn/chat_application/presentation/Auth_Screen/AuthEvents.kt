package com.vighn.chat_application.presentation.Auth_Screen

sealed class AuthEvents(){
    data class signUP_UsernameChanged(val value : String) : AuthEvents()
    data class signUP_PasswordChanged(val value : String) : AuthEvents()
    object signUP_ButtonPressed : AuthEvents()

    data class signIN_UsernameChanged(val value : String) : AuthEvents()
    data class signIN_PasswordChanged(val value : String) : AuthEvents()
    object signIN_ButtonPressed : AuthEvents()




}
