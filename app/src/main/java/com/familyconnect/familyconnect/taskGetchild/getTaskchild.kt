package com.familyconnect.familyconnect.taskGetchild

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GetTaskScreenchild(
    username: String?,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        username?.let { viewModel.fetchTasks(it) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            isLoading -> LoadingScreen()
            !errorMessage.isNullOrEmpty() -> ErrorScreen(errorMessage = errorMessage!!)
            else -> tasks?.let {
                if (it.isEmpty()) {
                    NoTasksScreen()
                } else {
                    TaskList(tasks = it, onTaskComplete = { username, taskId ->
                        Log.d("TaskList main ", "Sending taskId: $taskId for completion")
                        viewModel.completeTask(username, taskId)
                    })
                }
            }
            }
        }
    }


@Composable
fun LoadingScreen() {
    Text(
        text = "Loading...",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun ErrorScreen(errorMessage: String) {
    Text(
        text = "Error: $errorMessage",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun NoTasksScreen() {
    Text(
        text = "No tasks assigned",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskList(tasks: List<Task>, onTaskComplete: (String, Int) -> Unit) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(task = task, onTaskComplete = onTaskComplete)
        }
    }
}


val customFont = TextStyle(fontSize = 24.sp);


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(task: Task, onTaskComplete: (String, Int) -> Unit) {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")  // Custom pattern for year, month, and day
    val formattedDate = try {
        OffsetDateTime.parse(task.taskDueDate).format(dateTimeFormatter)
    } catch (e: Exception) {
        task.taskDueDate  // Fallback to original date if parsing fails
    }

    var showDetails by remember { mutableStateOf(false) }
    val backgroundColor = when (task.status) {
        "IN_PROGRESS" -> Color.Yellow
        "PENDING" -> Color.Blue
        "FAILED" -> Color.Red
        "COMPLETED" -> Color.Green
        else -> Color.Gray  // Default color if none of the statuses match
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.medium)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Task ID: ${task.taskId} - ${task.taskName}", style = customFont)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Due: $formattedDate", style = customFont)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Points: ${task.taskRewardPoints}", style = customFont)
            Spacer(modifier = Modifier.height(4.dp))

            if (task.status == "PENDING") {
                Text(text = "WAITING FOR PARENTS APPROVAL", style = customFont, color = Color.White)
            }
            else if (task.status == "FAILED") {
                Text(text = "PARENT REJECTED", style = customFont, color = Color.White)
            }


            Spacer(modifier = Modifier.height(12.dp))
            if (task.status == "IN_PROGRESS") {
                Row {
                    Button(
                        onClick = { onTaskComplete(task.taskAssigneeUserName, task.taskId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Complete Task", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { showDetails = !showDetails },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Details", color = Color.White)
                    }
                }
            } else if (task.status != "COMPLETED") {
                Button(
                    onClick = { showDetails = !showDetails },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Details", color = Color.White)
                }
            }
        }
    }

    if (showDetails) {
        AlertDialog(
            onDismissRequest = { showDetails = false },
            title = { Text("Task Description") },
            text = { Text(task.taskDescription) },
            confirmButton = {
                TextButton(
                    onClick = { showDetails = false }
                ) {
                    Text("Close")
                }
            }
        )
    }
}



