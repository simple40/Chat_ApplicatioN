package com.vighn.chat_application.presentation.Auth_Screen

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vighn.chat_application.Auth.models.AuthResult
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigator : NavController
) {
    val state = viewModel.authState
    val context = LocalContext.current
    LaunchedEffect(key1 = true ){
        viewModel.authResult.collectLatest { authResult->
            when(authResult){
                is AuthResult.Authorized ->{
                    println("Inside AUth_screen Authorized")
                    Toast.makeText(context,"welcome ${viewModel.username}",Toast.LENGTH_SHORT)
                    navigator.navigate("chat_screen/${viewModel.username}"){
                        popUpTo("auth_screen"){
                            inclusive=true
                        }
                    }

                }
                is AuthResult.Unauthorized ->{
                    Toast.makeText(context,"You are not authorized",Toast.LENGTH_SHORT)
                    println("Inside AUth_screen unauthorized")
                }
                is AuthResult.UnknownError ->{
                    Toast.makeText(context,"Unknown Error",Toast.LENGTH_SHORT)
                    println("Inside AUth_screen unknownerror")
                }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        
        TextField(value = state.value.signInUsername,
            onValueChange ={
                viewModel.onEvent(AuthEvents.signIN_UsernameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "username")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = state.value.signInPassword,
            onValueChange = {
                viewModel.onEvent(AuthEvents.signIN_PasswordChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "password")
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { 
            viewModel.onEvent(AuthEvents.signIN_ButtonPressed)
        },
            modifier = Modifier.align(Alignment.End)
            ) {
            Text(text = "Sign in")
        }


        Spacer(modifier = Modifier.height(64.dp))


        TextField(value = state.value.signUpUsername,
            onValueChange ={
                viewModel.onEvent(AuthEvents.signUP_UsernameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "username")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = state.value.signUpPassword,
            onValueChange = {
                viewModel.onEvent(AuthEvents.signUP_PasswordChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "password")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.onEvent(AuthEvents.signUP_ButtonPressed)
        },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Sign up")
        }

        if(state.value.isLoading){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        
    }
}