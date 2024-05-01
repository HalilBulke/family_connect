package com.familyconnect.familyconnect.profile

import com.familyconnect.familyconnect.login.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApiService {
    @GET("user/getUser/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>

    @PUT("profile/updateName/{username}/{newName}")
    suspend fun updateName(
        @Path("username") username: String,
        @Path("newName") newName: String
    ): Response<Void>

    @PUT("profile/updatePassword/{username}/{oldPassword}/{newPassword}")
    suspend fun updatePassword(
        @Path("username") username: String,
        @Path("oldPassword") oldPassword: String,
        @Path("newPassword") newPassword: String
    ): Response<Void>
}
