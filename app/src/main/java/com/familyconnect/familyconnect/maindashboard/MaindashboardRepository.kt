package com.familyconnect.familyconnect.maindashboard

import com.familyconnect.familyconnect.login.User
import retrofit2.Response
import javax.inject.Inject

interface MainDashboardRepository {
    suspend fun getUser(userName: String): Response<User>
}

class DefaultMainDashboardRepository @Inject constructor(
    private val apiService: MainDashboardApiService
) : MainDashboardRepository {

    override suspend fun getUser(userName: String): Response<User> {
        return apiService.getUser(userName)
    }
}

