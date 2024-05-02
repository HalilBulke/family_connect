package com.familyconnect.familyconnect.allProgress

import com.familyconnect.familyconnect.progressGetChild.Progress
import retrofit2.Response
import javax.inject.Inject

interface AllProgressRepository {
    suspend fun getAllProgress(userName: String): Response<List<Progress>>

    suspend fun completeProgress(userName: String, progressId: Int): Response<Progress>

    suspend fun cancelProgress(userName: String, progressId: Int): Response<Progress>

}

class NetworkAllProgressRepository @Inject constructor(
    private val allProgressApiService: AllProgressApiService
) : AllProgressRepository {
    override suspend fun getAllProgress(userName: String): Response<List<Progress>> {
        return allProgressApiService.getAllProgress(userName)
    }

    override suspend fun completeProgress(
        userName: String,
        progressId: Int
    ): Response<Progress> {
        return allProgressApiService.completeProgress(userName, progressId)
    }

    override suspend fun cancelProgress(
        userName: String,
        progressId: Int
    ): Response<Progress> {
        return allProgressApiService.cancelProgress(userName, progressId)
    }
}