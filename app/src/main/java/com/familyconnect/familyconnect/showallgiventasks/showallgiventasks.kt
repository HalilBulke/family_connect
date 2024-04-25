package com.familyconnect.familyconnect.showallgiventasks



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel



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
                        Text(text = "- ${task.taskName}: ${task.taskDescription}")
                    } ?: Text(text = "Loading tasks for $member...")
                }
            }
        }
    }
}