package com.familyconnect.familyconnect.displayfamily

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
fun MyFamilyScreen(
    username: String?,
    viewModel: MyFamilyViewModel = hiltViewModel()
) {
    val familyData by viewModel.familyData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFamily(username.toString())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isLoading == true) {
            // Display loading indicator
            Text(text = "Loading...")
        } else {
            if (!errorMessage.isNullOrEmpty()) {
                // Display error message if available
                Text(text = "Error: $errorMessage")
            } else {
                // Display family data if available
                familyData?.let { family ->
                    Text(text = "Family Name: ${family.familyName}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Family Members:")
                    family.familyMembers.forEach { member ->
                        Text(text = "- $member")
                    }
                }
            }
        }
    }
}
