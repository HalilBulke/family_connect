package com.familyconnect.familyconnect.register

import retrofit2.Response
import retrofit2.http.POST

interface RegisterApiService {
    @POST("/register")
    suspend fun register(): Response<Unit>
}