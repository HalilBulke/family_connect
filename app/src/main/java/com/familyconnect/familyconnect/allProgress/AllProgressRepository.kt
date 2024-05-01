package com.familyconnect.familyconnect.allProgress

import com.familyconnect.familyconnect.progressGetChild.Progress
import retrofit2.Response
import javax.inject.Inject

interface AllProgressRepository {
    suspend fun getAllProgress(userName: String): Response<List<Progress>>
}

class NetworkAllProgressRepository @Inject constructor(
    private val allProgressApiService: AllProgressApiService
) : AllProgressRepository {
    override suspend fun getAllProgress(userName: String): Response<List<Progress>> {
        return allProgressApiService.getAllProgress(userName)
    }
}