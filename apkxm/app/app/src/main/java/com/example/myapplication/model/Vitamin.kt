package com.example.myapplication.model

data class Vitamin(
    val id: Int,
    val name: String,
    val name_en: String?,
    val alias: String?,
    val description: String?,
    val function: String,
    val benefits: String?,
    val deficiency_symptoms: String?,
    val food_sources: String?,
    val daily_requirement: String?,
    val overdose_symptoms: String?,
    val image_url: String?,
    val is_active: Boolean,
    val created_at: String,
    val updated_at: String
)

