package com.familyconnect.familyconnect.progressGetChild

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ProgressApiService {
    @GET("progress/getByUserId/{userName}")
    suspend fun getProgressByUsername(@Path("userName") userName: String): Response<List<Progress>>
}
