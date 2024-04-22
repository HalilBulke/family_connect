package com.familyconnect.familyconnect.taskGetchild



import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskApiService {

    // GET method to retrieve tasks by username
    @GET("task/getTasks/{username}")
    suspend fun getTasksByUsername(
        @Path("username") username: String
    ): Response<List<TaskRequestBody>>
}