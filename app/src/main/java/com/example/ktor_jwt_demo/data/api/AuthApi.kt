package com.example.ktor_jwt_demo.data.api

import android.util.Log
import com.example.ktor_jwt_demo.data.SECRET_ENDPOINT
import com.example.ktor_jwt_demo.data.SIGN_IN_ENDPOINT
import com.example.ktor_jwt_demo.data.SIGN_UP_ENDPOINT
import com.example.ktor_jwt_demo.data.request.AuthRequest
import com.example.ktor_jwt_demo.data.request.SecretRequest
import com.example.ktor_jwt_demo.data.response.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class AuthApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun signUp(
        request: AuthRequest
    ): Result<String> {
        return try {
            val response = client.post(SIGN_UP_ENDPOINT) {
                setBody(request)
            }
             when (response.status.value) {
                200 -> {
                    Result.success("成功")
                }
                409 -> {
                    Result.failure(Exception(response.body<String>()))
                }
                else -> {
                    Result.failure(Exception("请求失败"))
                }
            }
        } catch (e: Exception) {
            Log.e("JWTapi", "signUp: ${e.message}")
            Result.failure(Exception("有什么出错了"))
        }
    }

    suspend fun signIn(
        request: AuthRequest
    ): Result<AuthResponse> {
        return try {
            val response = client.post(SIGN_IN_ENDPOINT) {
                setBody(request)
            }
            when (response.status.value) {
                200 -> {
                    Result.success(response.body())
                }
                409 -> {
                    Result.failure(Exception(response.body<String>()))
                }
                else -> {
                    Result.failure(Exception("请求失败"))
                }
            }
        } catch (e: Exception) {
            Log.e("JWTapi", "signIn: ${e.message}")
            Result.failure(Exception("有什么出错了"))
        }
    }

    suspend fun secret(
        request: SecretRequest
    ): Result<String>{
        return try {
            val response = client.get(SECRET_ENDPOINT) {
                bearerAuth(request.token)
            }
            when (response.status.value) {
                200 -> {
                    Result.success(response.body())
                }
                401 -> {
                    Result.failure(Exception("未授权"))
                }
                else -> {
                    Result.failure(Exception("请求失败"))
                }
            }
        } catch (e: Exception) {
            Log.e("JWTapi", "secret: ${e.message}")
            Result.failure(Exception("有什么出错了"))
        }
    }
}