package com.familyconnect.familyconnect.taskGetchild

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApiService {

    @GET("/task/getTasks/{username}")
    suspend fun getTasksByUsername(@Path("username") username: String): Response<List<Task>>

    @POST("/task/pendingTask/{username}/{taskId}")
    suspend fun acceptTask(@Path("username") userName: String, @Path("taskId") taskId: Int): Response<Task>

    @POST("/task/rejectTask/{username}/{taskId}")
    suspend fun rejectTask(@Path("username") username: String, @Path("taskId") taskId: Int): Response<Task>

}
