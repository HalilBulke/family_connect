package com.familyconnect.familyconnect.allProgress

import com.familyconnect.familyconnect.progressGetChild.Progress
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AllProgressApiService {
    @GET("/progress/getFamilyAll/{username}")
    suspend fun getAllProgress(@Path("username") username: String): Response<List<Progress>>
}