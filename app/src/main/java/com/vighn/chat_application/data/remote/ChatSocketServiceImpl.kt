package com.vighn.chat_application.data.remote

import android.content.SharedPreferences
import com.vighn.chat_application.data.remote.dto.MessageDTO
import com.vighn.chat_application.domain.model.Message
import com.vighn.chat_application.util.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    val client: HttpClient,
    private val preferences: SharedPreferences
) : ChatSocketService{
    private var socket : WebSocketSession?=null
    override suspend fun initSession(username: String): Resource<Unit> {
        return try{
            val token = preferences.getString("jwt",null)
            socket=client.webSocketSession {
                url("${ChatSocketService.Endpoints.ChatSocket.url}?username=$username")
                    headers{
                        append(HttpHeaders.Authorization , "Bearer $token")
                    }
            }
            if(socket?.isActive == true){
                Resource.Success(Unit)
            }
            else{
                Resource.Error(message = "Unable to make connection")
            }

        }catch (e: Exception){
            Resource.Error(message = e.localizedMessage ?: "Unknown Error")
        }

    }


    override suspend fun sendMessage(message: String) {
        try{
            socket?.send(Frame.Text(message))
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun observerMessages(): Flow<Message> {
        return try{
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto= Json.decodeFromString<MessageDTO>(json)
                    messageDto.toMessage()
                } ?: flow { }
        }catch (e : Exception){
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}