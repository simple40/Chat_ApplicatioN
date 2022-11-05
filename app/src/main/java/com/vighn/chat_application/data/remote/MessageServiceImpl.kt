package com.vighn.chat_application.data.remote

import android.content.SharedPreferences
import com.vighn.chat_application.data.remote.dto.MessageDTO
import com.vighn.chat_application.domain.model.Message
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.http.*

class MessageServiceImpl(
    private val client : HttpClient,
    val preferences: SharedPreferences
) : MessageService  {
    override suspend fun getAllMessages(): List<Message> {
        val token = preferences.getString("jwt",null)
        return try {
            client.get<List<MessageDTO>>(MessageService.Endpoints.GetAllMessages.url){
                    headers{
                        append(HttpHeaders.Authorization , "Bearer $token")
                    }
            }.map {
                it.toMessage()
            }

        }catch (e: Exception){
            emptyList()
        }
    }
}