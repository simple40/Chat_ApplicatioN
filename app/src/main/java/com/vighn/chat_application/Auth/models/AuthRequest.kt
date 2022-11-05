package com.vighn.chat_application.Auth.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username : String,
    val password : String
)
