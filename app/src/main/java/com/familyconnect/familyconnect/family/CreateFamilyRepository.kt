package com.familyconnect.familyconnect.family

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import javax.inject.Inject

interface FamilyRepository {
    suspend fun createFamily(family: CreateFamilyRequestBody): Response<FamilyResponse>
}

class NetworkFamilyRepository @Inject constructor(
    private val familyApiService: CreateFamilyApiService
) : FamilyRepository {

    override suspend fun createFamily(family: CreateFamilyRequestBody): Response<FamilyResponse> {
        return familyApiService.createFamily(family)
    }
}

data class CreateFamilyRequestBody(
    @SerializedName("familyName") val familyName: String,
    @SerializedName("familyCreatorUserName") val familyCreatorUserName: String
)

data class FamilyResponse(
    val success: Boolean,
    val message: String,
    val familyId: Int? = null
)
