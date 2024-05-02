package com.familyconnect.familyconnect.familyRewards

import com.familyconnect.familyconnect.displayfamily.FamilySpinDataDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FamilyRewardsApiService {
    @GET("/family/getFamilyRewards")
    suspend fun getFamilyRewards(@Query("username") userName: String): Response<List<Reward>>

    @GET("/family/getUserRewards")
    suspend fun getUserRewards(@Query("username") userName: String): Response<List<Reward>>
}