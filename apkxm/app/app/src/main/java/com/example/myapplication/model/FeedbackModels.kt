package com.example.myapplication.model

data class Feedback(
    val id: Int,
    val user_id: Int?,
    val title: String,
    val content: String,
    val contact: String?,
    val status: String,
    val created_at: String,
    val updated_at: String
)

data class FeedbackCreate(
    val title: String,
    val content: String,
    val contact: String?
)

data class FeedbackReply(
    val id: Int,
    val feedback_id: Int,
    val user_id: Int?,
    val content: String,
    val created_at: String
)

data class FeedbackReplyCreate(
    val content: String
)


