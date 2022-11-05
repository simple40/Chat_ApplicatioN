package com.vighn.chat_application.Auth.models

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token : String
)
