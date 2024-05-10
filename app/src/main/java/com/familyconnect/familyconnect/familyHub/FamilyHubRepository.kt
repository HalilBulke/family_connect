package com.familyconnect.familyconnect.familyHub

import retrofit2.Response
import javax.inject.Inject


interface FamilyHubRepository {
    suspend fun sendMessage(message: Message): Response<Message>

    suspend fun sendSurvey(survey: Survey): Response<Survey>

    suspend fun voteSurvey(vote: Vote): Response<Vote>

    suspend fun getMessages(userName: String): Response<List<ChatBaseMessage>>

}

class NetworkFamilyHubRepository @Inject constructor(
    private val familyHubApiService: FamilyHubApiService
) : FamilyHubRepository {
    override suspend fun sendMessage(message: Message): Response<Message> {
        return familyHubApiService.sendMessage(message)
    }

    override suspend fun sendSurvey(survey: Survey): Response<Survey> {
        return familyHubApiService.sendSurvey(survey)
    }

    override suspend fun voteSurvey(vote: Vote): Response<Vote> {
        return familyHubApiService.voteSurvey(vote)
    }

    override suspend fun getMessages(userName: String): Response<List<ChatBaseMessage>> {
        return familyHubApiService.getMessages(userName)
    }
}

data class Message(
    val message: String,
    val sender: String,
    val timestamp: String,
)

data class Survey(
    val sender: String,
    val description: String,
    val survey: List<String>,
    val timestamp: String,
)

data class Vote(
    val surveyId: Int,
    val username: String,
    val option: Int,
)

data class ChatBaseMessage(
    val id: Int,
    val type: String,
    val senderUsername: String,
    val senderName: String,
    val message: String,
    val description: String,
    val surveyVotes: List<SurveyFrontDTO>,
    val timestamp: String,
)

data class SurveyFrontDTO (
    val option: String,
    val voters: List<String>,
)

