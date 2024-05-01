package com.familyconnect.familyconnect.createprogress

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.commoncomposables.DropDownFun
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.ItemCard
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.util.makeToast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateProgressScreen(
    viewModel: CreateProgressViewModel = hiltViewModel(),
    username: String,
    onOkButtonClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is CreateProgressUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onOkButtonClicked() },
            )
        }
        is CreateProgressUiState.Loading -> {
            LoadingScreen()
        }
        is CreateProgressUiState.Success -> {
            makeToast(LocalContext.current, "New Progress Created Successfully")
        }

        is CreateProgressUiState.Default -> {
            CreateProgressPage(
                viewModel = viewModel,
                username = username,
                familyMembers = (uiState as CreateProgressUiState.Default).familyMembers,
                onOkButtonClicked = { onOkButtonClicked() },
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProgressPage(
    viewModel: CreateProgressViewModel = hiltViewModel(),
    username: String,
    familyMembers: List<String>,
    onOkButtonClicked: () -> Unit,
) {
    var progressName by remember { mutableStateOf("") }
    var quota by remember { mutableStateOf("") }
    var currentStatus by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pageColor = Color(0xFFE0724F)

    val dateTime = LocalDateTime.now()

    val datePickerState = remember {
        DatePickerState(
            yearRange = (2023..2024),
            initialSelectedDateMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            initialDisplayMode = DisplayMode.Input,
            initialDisplayedMonthMillis = null
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TopAppBar(
            title = { Text(text = "Create Progress") },
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
        ItemCard(
            Modifier.padding(8.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.create_progress),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentScale = ContentScale.Fit,
                )
                Text(
                    text = "Create Progress, Foster Growth: Empower Your Child's Development",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF000000),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AppInputField(
                value = progressName,
                onValueChange = { progressName = it },
                placeholderText = "Progress Name",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = pageColor,
                    unfocusedBorderColor = pageColor,
                ),
            )
            AppInputField(
                value = quota,
                onValueChange = { quota = it },
                placeholderText = "Quota",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = pageColor,
                    unfocusedBorderColor = pageColor,
                ),
            )
            AppInputField(
                value = currentStatus,
                onValueChange = { currentStatus = it },
                placeholderText = "Current Status",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = pageColor,
                    unfocusedBorderColor = pageColor,
                ),
            )
            DatePicker(
                state = datePickerState,
                title = null,
                modifier = Modifier.fillMaxWidth()
            )
            val selectedDate = datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
            }

            DropDownFun(
                userList = familyMembers,
                title = "Select a user",
                selectedUser = assignedTo,
                onValueChange = { selectedUser ->
                    assignedTo = selectedUser
                },
                color = pageColor,
            )
            AppButton(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                buttonText = "Create Progress",
                isLoading = false,
                onClick = {
                    val progress = CreateProgressRequestBody(
                        progressName = progressName,
                        quota = quota.toIntOrNull() ?: 0,
                        currentStatus = currentStatus.toIntOrNull() ?: 0,
                        dueDate = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE).toString(),
                        createdBy = username,
                        assignedTo = assignedTo
                    )
                    viewModel.addProgress(progress)
                },
                buttonColor = pageColor,
            )
        }
    }
}
