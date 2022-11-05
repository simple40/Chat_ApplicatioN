package com.vighn.chat_application.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vighn.chat_application.data.remote.ChatSocketService
import com.vighn.chat_application.data.remote.MessageService
import com.vighn.chat_application.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _messageText = mutableStateOf("")
    val messageText : State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state : State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent =_toastEvent.asSharedFlow()

    fun onMessageChange(message : String){
        _messageText.value=message
    }



    fun connectToChat() {
        getAllMessages()
        viewModelScope.launch {
            savedStateHandle.get<String>("username")?.let {
                val result = chatSocketService.initSession(it)
                when(result){
                    is Resource.Success ->{
                        chatSocketService.observerMessages()
                            .onEach { message->
                                val newList = state.value.messages.toMutableList().apply {
                                    add(0,message)
                                }
                                _state.value=state.value.copy(
                                    messages = newList
                                )
                            }.launchIn(viewModelScope)

                    }
                    is Resource.Error ->{
                        _toastEvent.emit(result.message ?: "Unknown Error")
                    }
                }

            }
        }
    }

    fun disconnect(){
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun getAllMessages(){

        viewModelScope.launch {
            _state.value=state.value.copy(isLoading = true)
            val all_messages=messageService.getAllMessages()
            _state.value=state.value.copy(
                messages = all_messages,
                isLoading = false
            )

        }

    }

    fun sendMessage(){
        viewModelScope.launch {
            if(messageText.value.isNotBlank()){
                chatSocketService.sendMessage(messageText.value)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }




}