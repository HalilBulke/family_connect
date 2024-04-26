package com.familyconnect.familyconnect.createprogress

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateProgressApiService {
    @POST("progress/create")
    suspend fun addProgress(
        @Body progress: CreateProgressRequestBody
    ): Response<ProgressResponse>
}