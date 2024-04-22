package com.familyconnect.familyconnect.displayfamily

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FamilyApiService {
    @GET("family/getFamily")
    suspend fun getFamily(@Query("userName") userName: String): Response<Family>
}
