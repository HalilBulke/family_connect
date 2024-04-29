package com.familyconnect.familyconnect.taskGetchild

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApiService {
    @GET("task/getTasks/{username}")
    suspend fun getTasksByUsername(@Path("username") username: String): Response<List<Task>>


    @POST("/task/pendingTask/{username}/{taskId}")
    fun setTaskPending(@Path("username") username: String, @Path("taskId") taskId: Int): Call<Void>


}
