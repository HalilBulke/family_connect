package com.familyconnect.familyconnect.progressGetChild



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun GetProgressScreenchild(
    username: String?,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        username?.let { viewModel.fetchTasks(it) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isLoading) {
            Text(text = "Loading...", style = MaterialTheme.typography.bodyLarge)
        } else if (!errorMessage.isNullOrEmpty()) {
            Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
        } else {
            tasks?.let {
                if (it.isEmpty()) {
                    Text(text = "No progress assigned", style = MaterialTheme.typography.bodyLarge)
                } else {
                    it.forEach { task ->
                        ProgressItem(progress = task)
                    }
                }
            }
        }
    }
}


@Composable
fun ProgressItem(progress: Progress) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Progress: ${progress.progressName}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Current Status: ${progress.currentStatus}/${progress.quota}")
        Text(text = "Due: ${progress.dueDate}")
        Text(text = "Assigned To: ${progress.assignedTo}")
        Text(text = "Created by: ${progress.createdBy}")
    }
}

