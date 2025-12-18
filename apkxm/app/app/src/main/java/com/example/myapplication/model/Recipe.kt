package com.example.myapplication.model

data class Recipe(
    val id: Int,
    val title: String,
    val description: String?,
    val ingredients: String,
    val steps: String,
    val image_url: String?,
    val calories: Double?,
    val protein: Double?,
    val carbs: Double?,
    val fat: Double?,
    val fiber: Double?,
    val category: String?,
    val difficulty: String,
    val prep_time: Int?,
    val cook_time: Int?,
    val servings: Int,
    val is_active: Boolean,
    val created_at: String,
    val updated_at: String
)

