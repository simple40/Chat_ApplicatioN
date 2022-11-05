package com.vighn.chat_application.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import com.vighn.chat_application.Auth.AuthService
import com.vighn.chat_application.Auth.AuthServiceImpl
import com.vighn.chat_application.data.remote.ChatSocketService
import com.vighn.chat_application.data.remote.ChatSocketServiceImpl
import com.vighn.chat_application.data.remote.MessageService
import com.vighn.chat_application.data.remote.MessageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClient() : HttpClient{
        return HttpClient(CIO){
            install(Logging)
            install(WebSockets)
            install(JsonFeature){
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun providesMessageService(client : HttpClient,prefs: SharedPreferences) : MessageService{
        return MessageServiceImpl(client,prefs)
    }

    @Provides
    @Singleton
    fun providesChatSocketService(client : HttpClient,prefs: SharedPreferences) : ChatSocketService {
        return ChatSocketServiceImpl(client,prefs)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(app : Application) : SharedPreferences {
        return app.getSharedPreferences("prefs" ,MODE_PRIVATE)

    }

    @Provides
    @Singleton
    fun provideAuthService(client : HttpClient , prefs : SharedPreferences) : AuthService{
        return AuthServiceImpl(client,prefs)
    }


}