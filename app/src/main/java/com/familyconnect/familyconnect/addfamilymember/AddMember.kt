package com.familyconnect.familyconnect.addfamilymember

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.ItemCard
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.util.makeToast

@Composable
fun AddFamilyMemberScreen(
    viewModel: AddFamilyMemberViewModel = hiltViewModel(),
    familyId : String?,
    onOkButtonClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is AddFamilyMemberUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onOkButtonClicked() },
            )
        }
        is AddFamilyMemberUiState.Loading -> {
            LoadingScreen()
        }
        is AddFamilyMemberUiState.Success -> {
            makeToast(LocalContext.current, "New family member added Successfully")
        }

        is AddFamilyMemberUiState.Default -> {
            AddFamilyMemberPage(
                familyId = familyId,
                onOkButtonClicked = onOkButtonClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFamilyMemberPage(
    viewModel: AddFamilyMemberViewModel = hiltViewModel(),
    familyId : String?,
    onOkButtonClicked: () -> Unit,
) {
    Log.d("FAMILYID"  ,familyId.toString())
    var userNames by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pageColor = Color(0xFFFFC107)

    Column {
        TopAppBar(
            title = { Text(text = "Add New Family Member") },
            navigationIcon = {
                IconButton(onClick = { onOkButtonClicked() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = pageColor,
                actionIconContentColor = pageColor,
                navigationIconContentColor = Color.White,
                scrolledContainerColor = pageColor,
                titleContentColor = Color(0xFFFFFFFF),
            )
        )
        ItemCard (
            Modifier.padding(4.dp)
        ){
            Column {
                Image(painter = painterResource(id = R.drawable.add_family_member), contentDescription = null)
                Text(
                    text = "Add a new family member with email",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF000000),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            AppInputField(
                value = userNames,
                onValueChange = { userNames = it },
                placeholderText = "Enter User Name",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = pageColor,
                    unfocusedBorderColor = pageColor,
                ),
            )

            AppButton(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                buttonText = "Add Family Member",
                onClick = {
                    viewModel.addFamilyMember(
                        AddFamilyMemberRequest(
                            familyId.toString().toInt(),
                            userNames.split(",")
                        )
                    )
                },
                buttonColor = pageColor,
                isLoading = false
            )
        }
    }
}

