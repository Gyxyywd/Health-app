package com.example.myapplication.model

data class WeightAssess(
    val bmi: Float,
    val level: String,
    val suggest_weight_range: List<Float>,
    val tips: List<String>
)


