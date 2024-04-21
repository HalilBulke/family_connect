package com.familyconnect.familyconnect.task

import com.google.gson.annotations.SerializedName
import javax.inject.Inject

// The TaskRepository interface
interface TaskRepository {
    suspend fun addTask(task: CreateTaskRequestBody): Result<TaskResponse>
}

// The TaskRepository implementation
class TaskRepositoryImpl @Inject constructor(
    private val createTaskApiService: CreateTaskApiService
) : TaskRepository {
    override suspend fun addTask(task: CreateTaskRequestBody): Result<TaskResponse> {
        return try {
            val response = createTaskApiService.addTask(task)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to add task: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


// Data class for the task request body
data class CreateTaskRequestBody(
    @SerializedName("taskName") val taskName: String,
    @SerializedName("taskDescription") val taskDescription: String,
    @SerializedName("taskCreatorUserName") val taskCreatorUserName: String,
    @SerializedName("taskAssigneeUserName") val taskAssigneeUserName: String,
    @SerializedName("taskDueDate") val taskDueDate: String,
    @SerializedName("taskRewardPoints") val taskRewardPoints: Int,
    @SerializedName("taskId") val taskId: Int
)

// Data class for the task response
data class TaskResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("taskId") val taskId: Int? = null
)

// Retrofit API service interface

