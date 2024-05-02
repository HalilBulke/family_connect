package com.familyconnect.familyconnect.displayfamily

import retrofit2.Response
import javax.inject.Inject

interface GetFamilyRepository {
    suspend fun getFamily(userName: String): Response<Family>

    suspend fun getFamilyMembers(userName: String): Response<List<FamilyMembers>>

    suspend fun getSpinWheels(userName: String): Response<List<SpinWheel>>

    suspend fun setReward(familySpinDataDTO: FamilySpinDataDTO): Response<FamilySpinDataDTO>

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

    override suspend fun getSpinWheels(userName: String): Response<List<SpinWheel>> {
        return familyApiService.getSpinWheels(userName)
    }

    override suspend fun setReward(familySpinDataDTO: FamilySpinDataDTO): Response<FamilySpinDataDTO> {
        return familyApiService.setReward(familySpinDataDTO)
    }
}

data class FamilySpinDataDTO(
    val id: Int,
    val username: String,
    val prize: String,
)

data class SpinWheel(
    val id: Int, // Change the type according to the actual type in the response
    val spinOwner: String,
    val spinRewards: List<String>, // Change the type according to the actual type in the response
)

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
