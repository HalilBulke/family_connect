package com.familyconnect.familyconnect.login


import retrofit2.Response

interface LoginRepository {
    suspend fun login(): Response<Unit>
}
class NetworkLoginRepository(
    private val loginApiService: LoginApiService
) : LoginRepository {
    override suspend fun login():
            Response<Unit> = loginApiService.login()
}