package com.familyconnect.familyconnect.displayfamily

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FamilyApiService {
    @GET("family/getFamily")
    suspend fun getFamily(@Query("userName") userName: String): Response<Family>

    @GET("family/getFamilyMembersInformation")
    suspend fun getFamilyMembers(@Query("userName") userName: String): Response<List<FamilyMembers>>

    @GET("family/getSpins")
    suspend fun getSpinWheels(@Query("username") userName: String): Response<List<SpinWheel>>


    @POST("family/setReward")
    suspend fun setReward(@Body familySpinDataDTO: FamilySpinDataDTO): Response<FamilySpinDataDTO>
}
