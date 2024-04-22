package com.familyconnect.familyconnect.taskGetchild

import com.google.gson.annotations.SerializedName


data class TaskRequestBody(
    @SerializedName("taskName") val taskName: String,
    @SerializedName("taskDescription") val taskDescription: String,
    @SerializedName("taskCreatorUserName") val taskCreatorUserName: String,
    @SerializedName("taskAssigneeUserName") val taskAssigneeUserName: String,
    @SerializedName("taskDueDate") val taskDueDate: String,
    @SerializedName("taskRewardPoints") val taskRewardPoints: Int,
    @SerializedName("taskId") val taskId: Int
)