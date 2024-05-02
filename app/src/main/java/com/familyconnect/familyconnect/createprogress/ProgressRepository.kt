package com.familyconnect.familyconnect.createprogress


import com.google.gson.annotations.SerializedName


import javax.inject.Inject



// The ProgressRepository interface
interface ProgressRepository {
    suspend fun addProgress(progress: CreateProgressRequestBody): Result<ProgressResponse>
}


// The ProgressRepository implementation
class ProgressRepositoryImpl @Inject constructor(
    private val createProgressApiService: CreateProgressApiService
) : ProgressRepository {
    override suspend fun addProgress(progress: CreateProgressRequestBody): Result<ProgressResponse> {
        return try {
            val response = createProgressApiService.addProgress(progress)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to add progress: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}





data class CreateProgressRequestBody(
    @SerializedName("progressName") val progressName: String,
    @SerializedName("quota") val quota: Int,
    @SerializedName("currentStatus") val currentStatus: Int,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("assignedTo") val assignedTo: String,
    @SerializedName("rewards") val rewards: List<String>,
)

data class ProgressResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("taskId") val taskId: Int? = null
)

