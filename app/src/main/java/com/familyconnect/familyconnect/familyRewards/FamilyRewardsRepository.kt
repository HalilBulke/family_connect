package com.familyconnect.familyconnect.familyRewards

import com.familyconnect.familyconnect.displayfamily.FamilySpinDataDTO
import retrofit2.Response
import javax.inject.Inject


interface FamilyRewardsRepository {

    suspend fun getFamilyRewards(userName: String): Response<List<Reward>>

    suspend fun getUserRewards(userName: String): Response<List<Reward>>

}

class FamilyRewardsRepositoryImpl @Inject constructor(
    private val familyRewardsApiService: FamilyRewardsApiService
) : FamilyRewardsRepository {
    override suspend fun getFamilyRewards(userName: String): Response<List<Reward>> {
        return familyRewardsApiService.getFamilyRewards(userName)
    }

    override suspend fun getUserRewards(userName: String): Response<List<Reward>> {
        return familyRewardsApiService.getUserRewards(userName)
    }
}

data class Reward(
    val id: Int,
    val rewardOwner: String,
    val reward: String,
)