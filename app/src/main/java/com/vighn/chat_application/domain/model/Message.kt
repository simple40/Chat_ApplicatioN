package com.vighn.chat_application.domain.model



data class Message(
    val text: String,
    val formattedTime: String,
    val userName: String
)
