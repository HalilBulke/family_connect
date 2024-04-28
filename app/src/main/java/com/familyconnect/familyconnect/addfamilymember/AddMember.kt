package com.familyconnect.familyconnect.addfamilymember

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.util.makeToast

@Composable
fun AddFamilyMemberScreen(
    viewModel: AddFamilyMemberViewModel = hiltViewModel(),
    familyId : String?,
    onOkButtonClicked: () -> Unit,
    onReTryButtonClicked:() -> Unit,
) {
    Log.d("FAMILYID"  ,familyId.toString())
    // Define state for family ID and list of user names to add
    var id by remember { mutableStateOf(0) }
    var userNames by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Define state for result of adding family member
    val result by viewModel.addFamilyMemberResult.observeAsState()
    var showError by remember { mutableStateOf(false) }
    // Call the ViewModel function to add family member when the button is clicked
    val onAddMemberClick: () -> Unit = {
        viewModel.addFamilyMember(AddFamilyMemberRequest(familyId.toString().toInt(), userNames.split(",")))
        if (result == null) {
            showError = true;
        }
    }

    // Display UI components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Add a new family member with email", style = MaterialTheme.typography.headlineMedium)

        AppInputField(
            value = userNames,
            onValueChange = { userNames = it },
            placeholderText = "Enter User Name",
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
            makeToast(context, "$userNames added Successfully")
            showError = false
        }

        if (showError && result == null) {
            val errorMessage = "User not found or already belongs to a family"
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
                title = "ERROR",
                description = errorMessage,
                firstButtonText = "RETURN",
                secondButtonText = "RETRY"
            )
            Log.d("wrong", errorMessage)
        }


    }
}

