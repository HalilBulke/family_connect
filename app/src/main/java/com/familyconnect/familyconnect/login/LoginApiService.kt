package com.familyconnect.familyconnect.login

import retrofit2.Response
import retrofit2.http.POST

interface LoginApiService {
    @POST("auth/login")
    suspend fun login(): Response<Unit>
}