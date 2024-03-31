package com.familyconnect.familyconnect.login


import retrofit2.Response

interface LoginRepository {
    suspend fun login(loginBody: LoginScreenPostItemBody): Response<LoginResponse>
}
class NetworkLoginRepository(
    private val loginApiService: LoginApiService
) : LoginRepository {
    override suspend fun login(loginBody: LoginScreenPostItemBody):
            Response<LoginResponse> = loginApiService.login(loginBody = loginBody)
}

data class LoginResponse(
    val user: User,
    val jwt: String
)

data class User(
    val userId: Int,
    val username: String,
    val password: String,
    val familyId: Int,
    val name: String,
    val authorities: List<Authority>,
    val enabled: Boolean,
    val accountNonLocked: Boolean,
    val accountNonExpired: Boolean,
    val credentialsNonExpired: Boolean
)

data class Authority(
    val roleId: Int,
    val authority: String
)