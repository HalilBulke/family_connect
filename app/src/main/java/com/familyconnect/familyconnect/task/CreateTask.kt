package com.familyconnect.familyconnect.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField

@Composable
fun CreateTaskScreen(viewModel: CreateTaskViewModel = hiltViewModel()) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskCreatorUserName by remember { mutableStateOf("") }
    var taskAssigneeUserName by remember { mutableStateOf("") }
    var taskDueDate by remember { mutableStateOf("") }
    var taskRewardPoints by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Create a New Task", style = MaterialTheme.typography.headlineMedium)
        AppInputField(
            value = taskName,
            onValueChange = { taskName = it },
            placeholderText = "Enter task name",
            isResponseError = false
        )
        AppInputField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            placeholderText = "Enter task description",
            isResponseError = false
        )
        AppInputField(
            value = taskCreatorUserName,
            onValueChange = { taskCreatorUserName = it },
            placeholderText = "Creator's username",
            isResponseError = false
        )
        AppInputField(
            value = taskAssigneeUserName,
            onValueChange = { taskAssigneeUserName = it },
            placeholderText = "Assignee's username",
            isResponseError = false
        )
        AppInputField(
            value = taskDueDate,
            onValueChange = { taskDueDate = it },
            placeholderText = "Due date (YYYY-MM-DD)",
            isResponseError = false
        )
        AppInputField(
            value = taskRewardPoints,
            onValueChange = { taskRewardPoints = it },
            placeholderText = "Reward Points",
            isResponseError = false,
            isPassword = false  // Assuming this is a numeric input, consider changing input type if needed
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            buttonText = "Create Task",
            isLoading = isLoading,
            onClick = {
                isLoading = true
                val task = CreateTaskRequestBody(
                    taskName = taskName,
                    taskDescription = taskDescription,
                    taskCreatorUserName = taskCreatorUserName,
                    taskAssigneeUserName = taskAssigneeUserName,
                    taskDueDate = taskDueDate,
                    taskRewardPoints.toIntOrNull() ?: 0,
                    taskId = 0  // Assuming new tasks have no initial ID.
                )
                viewModel.addTask(task, onSuccess = {
                    isLoading = false
                    // Navigate away or show success message
                }, onError = { error ->
                    isLoading = false
                    // Show error message
                })
            }
        )



    }
}

