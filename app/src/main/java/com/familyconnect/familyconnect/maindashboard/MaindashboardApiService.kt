package com.familyconnect.familyconnect.maindashboard

import com.familyconnect.familyconnect.login.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MainDashboardApiService {
    @GET("user/getUser/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>
}

