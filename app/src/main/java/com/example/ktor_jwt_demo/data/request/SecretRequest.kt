package com.example.ktor_jwt_demo.data.request

import kotlinx.serialization.Serializable

@Serializable
data class SecretRequest(
    val token: String
)
