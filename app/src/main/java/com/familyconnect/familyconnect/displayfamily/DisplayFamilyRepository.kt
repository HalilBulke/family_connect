package com.familyconnect.familyconnect.displayfamily

import retrofit2.Response
import javax.inject.Inject

interface GetFamilyRepository {
    suspend fun getFamily(userName: String): Response<Family>

    suspend fun getFamilyMembers(userName: String): Response<List<FamilyMembers>>

}

class DisplayFamilyRepository @Inject constructor(
    private val familyApiService: FamilyApiService
) : GetFamilyRepository {

    override suspend fun getFamily(userName: String): Response<Family> {
        return familyApiService.getFamily(userName)
    }

    override suspend fun getFamilyMembers(userName: String): Response<List<FamilyMembers>> {
        return familyApiService.getFamilyMembers(userName)
    }
}



data class Family(
    val id: Int, // Change the type according to the actual type in the response
    val familyName: String,
    val familyMembers: List<String>, // Change the type according to the actual type in the response
    val creatorUserName: String
)

data class FamilyMembers(
    val userName: String, // Change the type according to the actual type in the response
    val name: String,
    val profilePictureId: Int, // Change the type according to the actual type in the response
    val role: String,
)
