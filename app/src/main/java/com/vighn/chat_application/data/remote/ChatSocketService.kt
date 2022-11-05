package com.vighn.chat_application.data.remote

import com.vighn.chat_application.domain.model.Message
import com.vighn.chat_application.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(username : String) : Resource<Unit>

    suspend fun sendMessage(message : String)

    fun observerMessages() : Flow<Message>

    suspend fun closeSession()

    companion object{
        const val base_URL = "ws://chat-server-ktor.herokuapp.com"
    }

    sealed class Endpoints(val url : String){
        object ChatSocket: Endpoints("$base_URL/chat-socket")
    }
}