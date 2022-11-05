package com.vighn.chat_application.data.remote

import com.vighn.chat_application.domain.model.Message

interface MessageService {

    suspend fun getAllMessages() : List<Message>

    companion object{
        const val base_URL = "https://chat-server-ktor.herokuapp.com"
    }

    sealed class Endpoints(val url : String){
        object GetAllMessages: Endpoints("$base_URL/messages")
    }
}