package com.familyconnect.familyconnect.family

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateFamilyApiService {
    @POST("family/createFamily")
    suspend fun createFamily(
        @Body family: CreateFamilyRequestBody
    ): Response<FamilyResponse>
}
