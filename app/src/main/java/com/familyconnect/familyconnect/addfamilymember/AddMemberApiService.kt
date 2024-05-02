package com.familyconnect.familyconnect.addfamilymember

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AddMemberApiService {
    @POST("family/addFamilyMember")
    suspend fun addFamilyMember(@Query("familyId") familyId: Int,
                                @Body userNames: List<String>): Response<AddFamilyMemberResponse>

}
