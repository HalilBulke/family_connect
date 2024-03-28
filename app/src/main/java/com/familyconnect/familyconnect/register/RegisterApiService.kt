package com.familyconnect.familyconnect.register

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApiService {
    @POST("/auth/register")
    suspend fun register(
        @Body registerBody: RegisterScreenPostItemBody
    ): Response<Unit>
}