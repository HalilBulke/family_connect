package com.familyconnect.familyconnect.taskGetchild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


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
                    TaskList(tasks = it)
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

@Composable
fun TaskList(tasks: List<Task>) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(task = task)
        }
    }
}


val customFont = TextStyle(fontSize = 24.sp);

@Composable
fun TaskItem(task: Task) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = Color(0xFF00FF00),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Task: ${task.taskName}", style = customFont)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Description: ${task.taskDescription}", style = customFont)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Due: ${task.taskDueDate}", style = customFont)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Points: ${task.taskRewardPoints}",
                style = customFont,
            )
        }
    }
}


