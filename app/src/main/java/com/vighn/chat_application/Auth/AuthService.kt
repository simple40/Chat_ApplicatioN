package com.vighn.chat_application.Auth

import com.vighn.chat_application.Auth.models.AuthRequest
import com.vighn.chat_application.Auth.models.AuthResult
import com.vighn.chat_application.Auth.models.TokenResponse
import com.vighn.chat_application.data.remote.MessageService

interface AuthService {

    suspend fun signIN(authRequest: AuthRequest) : AuthResult<Unit>

    suspend fun signUP(authRequest: AuthRequest) : AuthResult<Unit>

    suspend fun authenticate() : AuthResult<Unit>

    companion object{
        const val base_URL = "https://chat-server-ktor.herokuapp.com"
    }

    sealed class Endpoints(val url : String){
        object signIN: Endpoints("${MessageService.base_URL}/signIN")
        object signUP: Endpoints("${MessageService.base_URL}/sighUP")
        object authenticate: Endpoints("${MessageService.base_URL}/authenticate")
    }

}