package com.familyconnect.familyconnect.taskGetchild

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import javax.inject.Inject

interface GetTasksRepository {
    suspend fun getTasksByUsername(userName: String): Response<List<Task>>
}

class TaskRepository @Inject constructor(
    private val taskApiService: TaskApiService
) : GetTasksRepository {
    override suspend fun getTasksByUsername(userName: String): Response<List<Task>> {
        return taskApiService.getTasksByUsername(userName)
    }
}



data class Task(
    @SerializedName("taskName") val taskName: String,
    @SerializedName("taskDescription") val taskDescription: String,
    @SerializedName("taskCreatorUserName") val taskCreatorUserName: String,
    @SerializedName("taskAssigneeUserName") val taskAssigneeUserName: String,
    @SerializedName("taskDueDate") val taskDueDate: String,
    @SerializedName("taskRewardPoints") val taskRewardPoints: Int,
    @SerializedName("taskId") val taskId: Int
)
