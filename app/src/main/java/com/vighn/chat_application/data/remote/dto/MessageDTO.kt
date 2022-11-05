package com.vighn.chat_application.data.remote.dto

import com.vighn.chat_application.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class MessageDTO(
    val text: String,
    val userName: String,
    val timeStamp: Long,
    val id: String
){
    fun toMessage () : Message{
        val date = Date(timeStamp)
        val formattedDate=DateFormat
            .getDateInstance(DateFormat.DEFAULT)
            .format(date)
        return Message(
            text = text,
            formattedTime = formattedDate,
            userName = userName
        )
    }
}
