package com.familyconnect.familyconnect.taskGetchild

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import javax.inject.Inject

interface GetTasksRepository {
    suspend fun getTasksByUsername(userName: String): Response<List<Task>>

    suspend fun acceptTask(userName: String, taskId: Int): Response<Task>

    suspend fun rejectTask(userName: String, taskId: Int): Response<Task>
}

class NetworkGetTasksRepository @Inject constructor(
    private val taskApiService: TaskApiService
) : GetTasksRepository {
    override suspend fun getTasksByUsername(userName: String): Response<List<Task>> {
        return taskApiService.getTasksByUsername(userName)
    }

    override suspend fun acceptTask(userName: String, taskId: Int): Response<Task> {
        return taskApiService.acceptTask(userName, taskId)
    }

    override suspend fun rejectTask(userName: String, taskId: Int): Response<Task> {
        return taskApiService.rejectTask(userName, taskId)
    }
}

data class Task(
    @SerializedName("taskName") val taskName: String,
    @SerializedName("taskDescription") val taskDescription: String,
    @SerializedName("taskCreatorUserName") val taskCreatorUserName: String,
    @SerializedName("taskAssigneeUserName") val taskAssigneeUserName: String,
    @SerializedName("taskDueDate") val taskDueDate: String,
    @SerializedName("id") val taskId: Int,
    @SerializedName("taskStatus") val status:String?,
    @SerializedName("taskRewardPoints") val taskRewardPoints: Int,
)
