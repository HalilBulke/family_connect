package com.familyconnect.familyconnect.progressGetChild


import com.google.gson.annotations.SerializedName
import retrofit2.Response
import javax.inject.Inject

interface GetProgressRepository {
    suspend fun getProgressByUsername(userName: String): Response<List<Progress>>
}

class ProgressRepository @Inject constructor(
    private val progressApiService: ProgressApiService
) : GetProgressRepository {
    override suspend fun getProgressByUsername(userName: String): Response<List<Progress>> {
        return progressApiService.getProgressByUsername(userName)
    }
}

data class Progress(
    @SerializedName("progressName") val progressName: String,
    @SerializedName("quota") val quota: Int,
    @SerializedName("currentStatus") val currentStatus: Int,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("assignedTo") val assignedTo: String,
    @SerializedName("progressId") val progressId: Int
)