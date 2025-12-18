package com.example.myapplication.model

data class PaginatedResponse<T>(
    val total: Int,
    val page: Int,
    val page_size: Int,
    val items: List<T>
)

