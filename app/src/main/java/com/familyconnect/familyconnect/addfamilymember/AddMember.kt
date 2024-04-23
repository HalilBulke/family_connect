package com.familyconnect.familyconnect.addfamilymember

import androidx.compose.foundation.layout.*
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

@Composable
fun AddFamilyMemberScreen(
    viewModel: AddFamilyMemberViewModel = hiltViewModel()
) {
    // Define state for family ID and list of user names to add
    var familyId by remember { mutableStateOf(0) }
    var userNames by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Define state for result of adding family member
    val result by viewModel.addFamilyMemberResult.observeAsState()

    // Call the ViewModel function to add family member when the button is clicked
    val onAddMemberClick: () -> Unit = {
        viewModel.addFamilyMember(AddFamilyMemberRequest(familyId, userNames.split(",")))
    }

    // Display UI components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Input fields for family ID and user names
        AppInputField(
            value = familyId.toString(),
            onValueChange = { familyId = it.toIntOrNull() ?: 0 },
            placeholderText = "Enter Family ID",
            isResponseError = false
        )
        AppInputField(
            value = userNames,
            onValueChange = { userNames = it },
            placeholderText = "Enter User Names (comma-separated)",
            isResponseError = false
        )

        // Button to add family member
        AppButton(
            buttonText = "Add Family Member",
            onClick = onAddMemberClick,
            isLoading = isLoading
        )

        // Display result message if available
        result?.let { response ->
            Text(
                text = response.message ?: "Added",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

