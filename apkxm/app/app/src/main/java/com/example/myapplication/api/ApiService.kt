package com.example.myapplication.api

import com.example.myapplication.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // 食谱相关
    @GET("recipes")
    suspend fun getRecipes(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
        @Query("category") category: String? = null
    ): Response<PaginatedResponse<Recipe>>
    
    @GET("recipes/{id}")
    suspend fun getRecipe(@Path("id") id: Int): Response<Recipe>
    
    // 维生素相关
    @GET("vitamins")
    suspend fun getVitamins(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): Response<PaginatedResponse<Vitamin>>
    
    @GET("vitamins/{id}")
    suspend fun getVitamin(@Path("id") id: Int): Response<Vitamin>
    
    // 健康小贴士相关
    @GET("health-tips")
    suspend fun getHealthTips(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): Response<PaginatedResponse<HealthTip>>
    
    @GET("health-tips/{id}")
    suspend fun getHealthTip(@Path("id") id: Int): Response<HealthTip>

    // 认证
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Token>

    @POST("auth/register")
    suspend fun register(@Body payload: RegisterPayload): Response<User>

    // 反馈
    @GET("feedbacks")
    suspend fun getFeedbacks(@Query("page") page: Int = 1, @Query("page_size") pageSize: Int = 10): Response<PaginatedResponse<Feedback>>

    @POST("feedbacks")
    suspend fun createFeedback(@Body payload: FeedbackCreate): Response<Feedback>

    @GET("feedbacks/{id}/replies")
    suspend fun getFeedbackReplies(@Path("id") id: Int): Response<List<FeedbackReply>>

    @POST("feedbacks/{id}/replies")
    suspend fun createFeedbackReply(@Path("id") id: Int, @Body payload: FeedbackReplyCreate): Response<FeedbackReply>

    // 工具
    @GET("tools/weight-assess")
    suspend fun weightAssess(
        @Query("height_cm") heightCm: Float,
        @Query("weight_kg") weightKg: Float,
        @Query("sex") sex: String = "male",
        @Query("age") age: Int = 25
    ): Response<WeightAssess>
}

