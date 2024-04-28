package com.familyconnect.familyconnect.createprogress

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
fun CreateProgressScreen(viewModel: CreateProgressViewModel = hiltViewModel(), username: String) {
    var progressName by remember { mutableStateOf("") }
    var quota by remember { mutableStateOf("") }
    var currentStatus by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var createdBy by remember { mutableStateOf(username) }  // Use provided username
    var assignedTo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Create Progress", style = MaterialTheme.typography.headlineMedium)
        AppInputField(
            value = progressName,
            onValueChange = { progressName = it },
            placeholderText = "Progresssssssssssssssss Name",
            isResponseError = false
        )
        AppInputField(
            value = quota,
            onValueChange = { quota = it },
            placeholderText = "Quota",
            isResponseError = false
        )
        AppInputField(
            value = currentStatus,
            onValueChange = { currentStatus = it },
            placeholderText = "Current Status",
            isResponseError = false
        )
        AppInputField(
            value = dueDate,
            onValueChange = { dueDate = it },
            placeholderText = "Due Date (YYYY-MM-DD)",
            isResponseError = false
        )
        AppInputField(
            value = assignedTo,
            onValueChange = { assignedTo = it },
            placeholderText = "Assigned To",
            isResponseError = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            buttonText = "Create Progress",
            isLoading = isLoading,
            onClick = {
                isLoading = true
                val progress = CreateProgressRequestBody(
                    progressName = progressName,
                    quota = quota.toIntOrNull() ?: 0,
                    currentStatus = currentStatus.toIntOrNull() ?: 0,
                    dueDate = dueDate,
                    createdBy = createdBy,
                    assignedTo = assignedTo
                )
                viewModel.addProgress(progress, onSuccess = {
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
