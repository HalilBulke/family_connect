package com.familyconnect.familyconnect.allProgress

import com.familyconnect.familyconnect.progressGetChild.Progress
import com.familyconnect.familyconnect.taskGetchild.Task
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AllProgressApiService {
    @GET("/progress/getFamilyAll/{username}")
    suspend fun getAllProgress(@Path("username") username: String): Response<List<Progress>>

    @POST("/progress/completeProgress/{username}/{progressId}")
    suspend fun completeProgress(@Path("username") userName: String, @Path("progressId") progressId: Int): Response<Progress>

    @POST("/progress/cancelProgress/{username}/{progressId}")
    suspend fun cancelProgress(@Path("username") userName: String, @Path("progressId") progressId: Int): Response<Progress>
}