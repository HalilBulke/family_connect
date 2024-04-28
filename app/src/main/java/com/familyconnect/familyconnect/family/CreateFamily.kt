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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.util.makeToast

@Composable
fun CreateFamilyScreen(viewModel: CreateFamilyViewModel = hiltViewModel(),
                       username : String?
) {
    var familyName by remember { mutableStateOf("") }
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
        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        AppButton(
            buttonText = "Create Family",
            isLoading = isLoading,
            onClick = {
                isLoading = true
                val family = CreateFamilyRequestBody(
                    familyName = familyName,
                    familyCreatorUserName = username.toString()
                )
                viewModel.createFamily(family, onSuccess = {
                    isLoading = false
                    makeToast(context, "Family $familyName Created Successfully")
                }, onError = { error ->

                    // TODO
                })
            }
        )
    }
}
