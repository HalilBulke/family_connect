package com.familyconnect.familyconnect.register

import retrofit2.Response

interface RegisterRepository {
    suspend fun register(registerBody: RegisterScreenPostItemBody): Response<Unit>
}
class NetworkRegisterRepository(
    private val registerApiService: RegisterApiService
) : RegisterRepository {
    override suspend fun register(registerBody: RegisterScreenPostItemBody):
            Response<Unit> = registerApiService.register(registerBody = registerBody)
}