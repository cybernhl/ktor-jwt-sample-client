package com.example.ktor_jwt_demo.data.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val username: String,
    val userId: String,
    val token: String
)
