package com.familyconnect.familyconnect.family

import androidx.compose.foundation.layout.*
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
fun CreateFamilyScreen(viewModel: CreateFamilyViewModel = hiltViewModel()) {
    var familyName by remember { mutableStateOf("") }
    var familyCreatorUserName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Create a New Family", style = MaterialTheme.typography.headlineMedium)
        AppInputField(
            value = familyName,
            onValueChange = { familyName = it },
            placeholderText = "Enter family name",
            isResponseError = false
        )
        AppInputField(
            value = familyCreatorUserName,
            onValueChange = { familyCreatorUserName = it },
            placeholderText = "Creator's username",
            isResponseError = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            buttonText = "Create Family",
            isLoading = isLoading,
            onClick = {
                isLoading = true
                val family = CreateFamilyRequestBody(
                    familyName = familyName,
                    familyCreatorUserName = familyCreatorUserName
                )
                viewModel.createFamily(family, onSuccess = {
                    isLoading = false
                    // TODO
                }, onError = { error ->

                    // TODO
                })
            }
        )
    }
}
