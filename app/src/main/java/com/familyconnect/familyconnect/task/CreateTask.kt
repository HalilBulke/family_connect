package com.familyconnect.familyconnect.task

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.commoncomposables.DropDownFun
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.util.makeToast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskScreen(
    viewModel: CreateTaskViewModel = hiltViewModel(),
    username: String,
    onOkButtonClicked: () -> Unit,
    onReTryButtonClicked:() -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is CreateTaskUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
                title = (uiState as CreateTaskUiState.Error).errorMessageTitle.orEmpty(),
                description = (uiState as CreateTaskUiState.Error).errorMessageDescription.orEmpty()
            )
        }
        is CreateTaskUiState.Loading -> {
            LoadingScreen()
        }
        is CreateTaskUiState.Success -> {
            CreateTaskPage(
                viewModel = viewModel,
                userName = username,
                familyMembers = (uiState as CreateTaskUiState.Success).familyMembers.orEmpty(),
                childNames = (uiState as CreateTaskUiState.Success).childNames.orEmpty(),
                childUserNames = (uiState as CreateTaskUiState.Success).childUserNames.orEmpty(),
                onOkButtonClicked = onOkButtonClicked
            )
        }
        is CreateTaskUiState.final -> {
            makeToast(LocalContext.current, "New Task Added Successfully")
            CreateTaskPage(
                viewModel = viewModel,
                userName = username,
                familyMembers = (uiState as CreateTaskUiState.final).familyMembers.orEmpty(),
                childNames = (uiState as CreateTaskUiState.final).childNames.orEmpty(),
                childUserNames = (uiState as CreateTaskUiState.final).childUserNames.orEmpty(),
                onOkButtonClicked = onOkButtonClicked
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskPage(
    viewModel: CreateTaskViewModel,
    userName: String,
    familyMembers: List<String>,
    childNames: List<String>,
    childUserNames: List<String>,
    onOkButtonClicked: () -> Unit,
) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskAssigneeUserName by remember { mutableStateOf("") }
    var taskRewardPoints by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Column {
        TopAppBar(
            title = { Text(text = "Create Task") },
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
                containerColor = Color(0xFF03A9F4),
                actionIconContentColor = Color(0xFF03A9F4),
                navigationIconContentColor = Color.White,
                scrolledContainerColor = Color(0xFF03A9F4),
                titleContentColor = Color(0xFFFFFFFF),
            )
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 8.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val dateTime = LocalDateTime.now()

            val datePickerState = remember {
                DatePickerState(
                    yearRange = (2023..2024),
                    initialSelectedDateMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    initialDisplayMode = DisplayMode.Input,
                    initialDisplayedMonthMillis = null
                )
            }

            Text(
                text ="Create tasks to contribute to the development of your child's sense of responsibility.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            AppInputField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholderText = "Enter task name",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF03A9F4),
                    unfocusedBorderColor = Color(0xFF03A9F4),
                ),
            )
            AppInputField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                placeholderText = "Enter task description",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF03A9F4),
                    unfocusedBorderColor = Color(0xFF03A9F4),
                ),
            )

            AppInputField(
                value = taskRewardPoints,
                onValueChange = { newValue ->
                    // Only allow numeric input that can be parsed as Int
                    if (newValue.all { it.isDigit() }) {
                        taskRewardPoints = newValue
                    }
                },
                placeholderText = "Reward Points",
                isResponseError = false,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF03A9F4),
                    unfocusedBorderColor = Color(0xFF03A9F4),
                ),
            )

            if (familyMembers.isNotEmpty()) {
                DropDownFun(
                    userList = childNames,
                    title = "Select a child",
                    selectedUser = taskAssigneeUserName,
                    onValueChange = { selectedUser ->
                        taskAssigneeUserName = selectedUser
                    },
                    Color(0xFF03A9F4)
                )
            }

            DropDownFun(
                userList = listOf("1","2","3","4"),
                title = "Select task Priority",
                selectedUser = priority,
                onValueChange = { selectedPriority ->
                    priority = selectedPriority
                },
                Color(0xFF03A9F4)
            )
            DatePicker(
                state = datePickerState,
                title = null,
                modifier = Modifier.fillMaxWidth()
            )
            val selectedDate = datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
            }

            Log.d("datePickerState", selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE).toString())
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Create Task",
                isLoading = false,
                onClick = {
                    val index = childNames.indexOf(taskAssigneeUserName)
                    val task = CreateTaskRequestBody(
                        taskName = taskName,
                        taskDescription = taskDescription,
                        taskCreatorUserName = userName,
                        taskAssigneeUserName = childUserNames[index],
                        taskDueDate = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE).toString(),
                        taskRewardPoints = taskRewardPoints.toIntOrNull() ?: 0,
                        priority = if (priority.isNotEmpty()) priority.toInt() else 0,
                    )

                    viewModel.addTask(task)
                },
                buttonColor = Color(0xFF00BCD4)
            )
        }
    }
}


