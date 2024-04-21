package com.familyconnect.familyconnect.task


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateTaskApiService {
    @POST("task/addTask")
    suspend fun addTask(
        @Body task: CreateTaskRequestBody
    ): Response<TaskResponse>
}
