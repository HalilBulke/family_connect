package com.familyconnect.familyconnect.taskGetchild


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp






fun getDummyTasks(username : String?): List<TaskRequestBody> {
    return listOf(
        TaskRequestBody("$username", "Complete the math homework", "Alice", "Bob", "2024-04-30", 50, 1),
        TaskRequestBody("Chores", "Wash the dishes", "Charlie", "Dave", "2024-05-01", 30, 2),
        TaskRequestBody("Exercise", "Run 5 kilometers", "Eve", "Frank", "2024-04-29", 20, 3)
    )
}




@Composable
fun GetTaskScreenchild(username : String?) {
    val tasks = getDummyTasks(username)  // Fetch the dummy tasks

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(tasks) { task ->
            TaskItem(task)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TaskItem(task: TaskRequestBody) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Task Name: ${task.taskName}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Description: ${task.taskDescription}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Assigned by: ${task.taskCreatorUserName}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Assigned to: ${task.taskAssigneeUserName}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Due Date: ${task.taskDueDate}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Reward Points: ${task.taskRewardPoints}", style = MaterialTheme.typography.bodySmall)
    }
}