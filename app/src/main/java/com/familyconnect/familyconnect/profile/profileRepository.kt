package com.familyconnect.familyconnect.profile

import com.familyconnect.familyconnect.login.User
import retrofit2.Response
import javax.inject.Inject

interface ProfileRepository {
    suspend fun getUser(userName: String): Response<User>

    suspend fun updateName(username: String, newName: String): Response<Void>

    suspend fun updatePassword(username: String, oldPassword: String, newPassword: String): Response<Void>

}

class NetworkProfileRepository @Inject constructor(
    private val apiService: ProfileApiService
) : ProfileRepository {

    override suspend fun getUser(userName: String): Response<User> {
        return apiService.getUser(userName)
    }

    override suspend fun updateName(username: String, newName: String): Response<Void> {
        return apiService.updateName(username, newName)
    }

    override suspend fun updatePassword(username: String, oldPassword: String, newPassword: String): Response<Void> {
        return apiService.updatePassword(username, oldPassword, newPassword)
    }
}
