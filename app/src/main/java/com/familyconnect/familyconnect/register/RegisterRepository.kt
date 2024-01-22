package com.familyconnect.familyconnect.register

import retrofit2.Response

interface RegisterRepository {
    suspend fun register(): Response<Unit>
}
class NetworkRegisterRepository(
    private val registerApiService: RegisterApiService
) : RegisterRepository {
    override suspend fun register():
            Response<Unit> = registerApiService.register()
}