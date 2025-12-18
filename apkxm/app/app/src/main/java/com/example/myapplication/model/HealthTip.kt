package com.example.myapplication.model

data class HealthTip(
    val id: Int,
    val title: String,
    val content: String,
    val category: String?,
    val image_url: String?,
    val is_active: Boolean,
    val created_at: String,
    val updated_at: String
)

