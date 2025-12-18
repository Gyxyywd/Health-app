package com.example.myapplication.model

data class Token(
    val access_token: String,
    val token_type: String
)

data class RegisterPayload(
    val username: String,
    val password: String
)

data class User(
    val id: Int,
    val username: String,
    val is_admin: Boolean,
    val created_at: String
)


