package com.vighn.chat_application.presentation.chat

import com.vighn.chat_application.domain.model.Message

data class ChatState(
    val messages : List<Message> = emptyList(),
    val isLoading : Boolean =false
)
