package com.vighn.chat_application.presentation.username

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNameViewModel @Inject constructor() : ViewModel() {

    private val _usernameTextField = mutableStateOf("")
    val usernameTextField : State<String> = _usernameTextField

    private val _onJoinChat = MutableSharedFlow<String>()
    val onJoinChat : SharedFlow<String> = _onJoinChat.asSharedFlow()

    fun onUsernameChange(username : String){
        _usernameTextField.value=username
    }

    fun onJoinClick() {
        viewModelScope.launch {
            if(usernameTextField.value.isNotBlank()){
                _onJoinChat.emit(usernameTextField.value)
            }
        }
    }

}