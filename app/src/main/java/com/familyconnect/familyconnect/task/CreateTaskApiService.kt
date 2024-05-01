package com.familyconnect.familyconnect.task


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.familyconnect.familyconnect.taskGetchild.Task

interface CreateTaskApiService {
    @POST("task/addTask")
    suspend fun addTask(
        @Body task: CreateTaskRequestBody
    ): Response<TaskResponse>

    @GET("task/getAllTasks/{username}")
    suspend fun getAllTasks(@Path("username") userName: String): Response<List<Task>>

    @POST("task/completeTask/{username}/{taskId}")
    suspend fun acceptTask(@Path("username") userName: String, @Path("taskId") taskId: Int): Response<Task>

    @POST("task/rejectTask/{username}/{taskId}")
    suspend fun rejectTask(@Path("username") userName: String, @Path("taskId") taskId: Int): Response<Task>
}
