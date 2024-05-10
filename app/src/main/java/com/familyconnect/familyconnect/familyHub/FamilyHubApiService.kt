package com.familyconnect.familyconnect.familyHub

import com.familyconnect.familyconnect.progressGetChild.Progress
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FamilyHubApiService {
    @POST("chat/sendMessage")
    suspend fun sendMessage(
        @Body message: Message
    ): Response<Message>

    @POST("chat/sendSurvey")
    suspend fun sendSurvey(
        @Body survey: Survey
    ): Response<Survey>

    @POST("chat/voteSurvey")
    suspend fun voteSurvey(
        @Body vote: Vote
    ): Response<Vote>

    @GET("chat/getMessages")
    suspend fun getMessages(
        @Query("username") username: String
    ): Response<List<ChatBaseMessage>>
}