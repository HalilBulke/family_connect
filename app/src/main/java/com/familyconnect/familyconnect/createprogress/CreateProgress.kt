package com.familyconnect.familyconnect.createprogress

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
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
    onReTryButtonClicked:() -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is CreateProgressUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
                title = (uiState as CreateProgressUiState.Error).errorMessageTitle.orEmpty(),
                description = (uiState as CreateProgressUiState.Error).errorMessageDescription.orEmpty()
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
                childNames = (uiState as CreateProgressUiState.Default).childNames.orEmpty(),
                childUserNames = (uiState as CreateProgressUiState.Default).childUserNames.orEmpty(),
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
    childNames: List<String>,
    childUserNames: List<String>,
    onOkButtonClicked: () -> Unit,
) {
    var progressName by remember { mutableStateOf("") }
    var quota by remember { mutableStateOf("") }
    var rewards by remember { mutableStateOf("") }
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

    val prizes = remember { mutableStateListOf("") }

    Column{
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
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                    onValueChange = { newValue ->
                        // Only allow numeric input that can be parsed as Int
                        if (newValue.all { it.isDigit() }) {
                            quota = newValue
                        }
                    },
                    placeholderText = "Quota",
                    isResponseError = false,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        focusedBorderColor = pageColor,
                        unfocusedBorderColor = pageColor,
                    ),
                )
                /*AppInputField(
                    value = rewards,
                    onValueChange = { rewards = it },
                    placeholderText = "Add Rewards",
                    isResponseError = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        focusedBorderColor = pageColor,
                        unfocusedBorderColor = pageColor,
                    ),
                )*/

                prizes.forEachIndexed { index, prize ->
                    PrizeTextField(
                        value = prize,
                        onValueChange = { newValue -> prizes[index] = newValue },
                        onDeleteOption = { prizes.removeAt(index) },
                        label = "Prize ${index + 1}",
                        pageColor = pageColor
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = pageColor
                    ),
                    onClick = { prizes.add("") }) {
                    Text(text = "Add Prize")
                }

                DatePicker(
                    state = datePickerState,
                    title = null,
                    modifier = Modifier.fillMaxWidth()
                )
                val selectedDate = datePickerState.selectedDateMillis?.let {
                    Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC)
                }

                DropDownFun(
                    userList = childNames,
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
                        val index = childNames.indexOf(assignedTo)
                        val progress = CreateProgressRequestBody(
                            progressName = progressName,
                            quota = quota.toIntOrNull() ?: 0,
                            currentStatus = 0,
                            dueDate = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE).toString(),
                            createdBy = username,
                            assignedTo = childUserNames[index],
                            rewards = prizes.filter { it.isNotBlank() },
                        )
                        viewModel.addProgress(progress)
                    },
                    buttonColor = pageColor,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrizeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onDeleteOption: () -> Unit,
    label: String,
    pageColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedTextColor = Color.Black,
                disabledBorderColor = Color.Gray,
                focusedBorderColor = pageColor,
                unfocusedBorderColor = pageColor,
            )
        )
        IconButton(
            onClick = onDeleteOption,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Prize",
                tint = pageColor
            )
        }
    }
}

@Preview
@Composable
fun PrizeTextFieldPreview() {
    PrizeTextField(
        value = "Prize value",
        onValueChange = { /* Implement onValueChange */ },
        onDeleteOption = { /* Implement onDeleteOption */ },
        label = "Prize",
        pageColor = Color(0xFFE0724F)
    )
}
