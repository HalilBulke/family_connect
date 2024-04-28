package com.familyconnect.familyconnect.task

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.commoncomposables.DropDownFun


@Composable
fun CreateTaskScreen(viewModel: CreateTaskViewModel = hiltViewModel(), username: String?) {


    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskCreatorUserName by remember { mutableStateOf(username) }  // Directly using the passed username, no need to show it in UI
    var taskAssigneeUserName by remember { mutableStateOf("") }
    var taskDueDate by remember { mutableStateOf("") }
    var taskRewardPoints by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }



    val familyData by viewModel.familyData.observeAsState()
    val isLoading2 by viewModel.isLoading2.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()


    LaunchedEffect(Unit) {
        viewModel.loadFamilyMembers(username.toString())
    }
    val userList = familyData?.familyMembers //user list



    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isLoading2 == true) {
            // Display loading indicator
            Text(text = "Loading...")
        } else {
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
                value = taskDueDate,
                onValueChange = { taskDueDate = it },
                placeholderText = "Due date (YYYY-MM-DD)",
                isResponseError = false
            )
            AppInputField(
                value = taskRewardPoints,
                onValueChange = { taskRewardPoints = it },
                placeholderText = "Reward Points",
                isResponseError = false
            )

            if (userList != null) {
                DropDownFun(
                    userList = userList,
                    selectedUser = taskAssigneeUserName,
                    onValueChange = { selectedUser ->
                        taskAssigneeUserName = selectedUser
                    }
                )
            }

            Spacer(modifier = Modifier.height(96.dp))
            AppButton(
                buttonText = "Create Task",
                isLoading = isLoading,
                onClick = {
                    Log.d("CreateTaskScreen", "Username: $username")
                    isLoading = true
                    val task = CreateTaskRequestBody(
                        taskName = taskName,
                        taskDescription = taskDescription,
                        taskCreatorUserName = taskCreatorUserName.toString(),  // This value is now entirely internal
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
}


