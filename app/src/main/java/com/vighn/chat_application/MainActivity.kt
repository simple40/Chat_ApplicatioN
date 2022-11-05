package com.vighn.chat_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vighn.chat_application.presentation.Auth_Screen.AuthScreen
import com.vighn.chat_application.presentation.chat.ChatScreen
import com.vighn.chat_application.presentation.username.UsernameScreen
import com.vighn.chat_application.ui.theme.Chat_ApplicatioNTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "auth_screen"){

                composable("auth_screen"){
                    AuthScreen(navigator = navController)
                }

                composable("username_screen"){
                    UsernameScreen(onNavigate = navController::navigate)
                }

                composable("chat_screen/{username}",
                    arguments = listOf(
                        navArgument(name = "username"){
                            type= NavType.StringType
                            nullable=true
                        }
                    )
                    ){
                        val username = it.arguments?.getString("username")
                        ChatScreen(username = username)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Chat_ApplicatioNTheme {
        Greeting("Android")
    }
}