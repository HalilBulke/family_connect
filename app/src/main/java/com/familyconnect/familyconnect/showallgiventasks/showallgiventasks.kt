package com.familyconnect.familyconnect.showallgiventasks



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.taskGetchild.Task

@Composable
fun ShowAllGivenTasks(
    username: String?,
    viewModel: MyFamilyViewModel2 = hiltViewModel()
) {
    val familyData by viewModel.familyData.observeAsState()
    val tasksData by viewModel.tasksData.observeAsState(emptyMap())
    val isLoading by viewModel.isLoading.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(username) {
        viewModel.fetchFamily(username.orEmpty())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isLoading == true) {
            Text(text = "Loading...")
        } else if (!errorMessage.isNullOrEmpty()) {
            Text(text = "Error: $errorMessage")
        } else {
            familyData?.let { family ->
                Text(text = "Family Name: ${family.familyName}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Family Members and their tasks:")
                family.familyMembers.forEach { member ->
                    Text(text = "$member's tasks:")
                    tasksData[member]?.forEach { task ->
                        TaskApprovalBox(task = task, viewModel = viewModel)
                    } ?: Text(text = "Loading tasks for $member...")
                }
            }
        }
    }
}

@Composable
fun TaskApprovalBox(task: Task, viewModel: MyFamilyViewModel2) {
    val (showDetails, setShowDetails) = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(color = when (task.status) {
                "PENDING" -> Color.Blue
                else -> Color.LightGray
            }, shape = MaterialTheme.shapes.medium)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "${task.taskName}: ${task.taskDescription}")
            Spacer(modifier = Modifier.height(4.dp))
            if (task.status == "PENDING") {
                Row {
                    Button(
                        onClick = { viewModel.completeTask(task.taskAssigneeUserName, task.taskId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Accept", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { viewModel.rejectTask(task.taskAssigneeUserName, task.taskId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Reject", color = Color.White)
                    }
                }
            }
        }
    }
}
