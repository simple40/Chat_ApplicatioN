package com.vighn.chat_application.Auth

import android.content.SharedPreferences
import com.vighn.chat_application.Auth.models.AuthRequest
import com.vighn.chat_application.Auth.models.AuthResult
import com.vighn.chat_application.Auth.models.TokenResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.get
import io.ktor.http.*


class AuthServiceImpl(
    private val client: HttpClient,
    private val preferences: SharedPreferences
    ) : AuthService {

    override suspend fun signUP(authRequest: AuthRequest): AuthResult<Unit> {
        return try {
            client.post<Unit>(AuthService.Endpoints.signUP.url){
                contentType(ContentType.Application.Json)
                body=authRequest
            }
            signIN(authRequest)
        }
        catch (e : ClientRequestException){
            val err = e.response
            if(err.status == HttpStatusCode.Unauthorized ){
                AuthResult.Unauthorized<Unit>()
            }
            AuthResult.UnknownError()
        }
        catch (e : Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIN(authRequest: AuthRequest): AuthResult<Unit> {
        return try {
            val token  = client.post<TokenResponse>(AuthService.Endpoints.signIN.url){
                contentType(ContentType.Application.Json)
                body=authRequest
            }
            preferences.edit().putString(
                "jwt",
                token.token
            ).apply()
            authenticate()
        }
        catch (e : ClientRequestException){
            val err = e.response
            if(err.status == HttpStatusCode.Unauthorized ){
                AuthResult.Unauthorized<Unit>()
            }
            AuthResult.UnknownError()
        }
        catch (e : Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = preferences.getString("jwt",null) ?: return AuthResult.Unauthorized()
            val username =client.get<String>(AuthService.Endpoints.authenticate.url){
                headers{
                    append(HttpHeaders.Authorization , "Bearer $token")
                }
            }
            preferences.edit().putString(
                "username",
                username
            ).apply()
            AuthResult.Authorized()
        }
        catch (e : ClientRequestException){
            val err = e.response
            if(err.status == HttpStatusCode.Unauthorized ){
                AuthResult.Unauthorized<Unit>()
            }
            AuthResult.UnknownError()
        }
        catch (e : Exception){
            AuthResult.UnknownError()
        }

    }
}