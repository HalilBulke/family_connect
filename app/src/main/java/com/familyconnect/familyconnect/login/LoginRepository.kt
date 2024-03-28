package com.familyconnect.familyconnect.login


import retrofit2.Response

interface LoginRepository {
    suspend fun login(loginBody: LoginScreenPostItemBody): Response<Unit>
}
class NetworkLoginRepository(
    private val loginApiService: LoginApiService
) : LoginRepository {
    override suspend fun login(loginBody: LoginScreenPostItemBody):
            Response<Unit> = loginApiService.login(loginBody = loginBody)
}