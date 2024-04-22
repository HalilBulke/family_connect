package com.familyconnect.familyconnect.displayfamily

import retrofit2.Response
import javax.inject.Inject

interface GetFamilyRepository {
    suspend fun getFamily(userName: String): Response<Family>
}

class DisplayFamilyRepository @Inject constructor(
    private val familyApiService: FamilyApiService
) : GetFamilyRepository {

    override suspend fun getFamily(userName: String): Response<Family> {
        return familyApiService.getFamily(userName)
    }
}



data class Family(
    val id: Int, // Change the type according to the actual type in the response
    val familyName: String,
    val familyMembers: List<String>, // Change the type according to the actual type in the response
    val creatorUserName: String
)

