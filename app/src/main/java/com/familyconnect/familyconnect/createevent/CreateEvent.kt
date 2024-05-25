package com.familyconnect.familyconnect.createevent

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.family.CreateFamilyUiState
import com.familyconnect.familyconnect.util.makeToast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateEventScreen(
    viewModel: CreateEventViewModel = hiltViewModel(),
    username: String,
    familyId: String,
    onOkButtonClicked: () -> Unit,
    onReTryButtonClicked: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    when (uiState) {
        is CreateEventUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
                title = (uiState as CreateEventUiState.Error).errorMessageTitle.orEmpty(),
                description = (uiState as CreateEventUiState.Error).errorMessageDescription.orEmpty()
            )
        }
        is CreateEventUiState.Loading -> {
            LoadingScreen()
        }
        is CreateEventUiState.Success -> {
            makeToast(context, "Event created Successfully")
        }

        is CreateEventUiState.Default -> {
            CreateEventPage(
                viewModel = viewModel,
                userName = username,
                onOkButtonClicked = onOkButtonClicked,
                familyId = familyId.toInt()
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateEventPage(
    viewModel: CreateEventViewModel,
    userName: String,
    onOkButtonClicked: () -> Unit,
    familyId: Int,
) {
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Column {
        TopAppBar(
            title = { Text(text = "Create Event") },
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
                    initialSelectedDateMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli(),
                    initialDisplayMode = DisplayMode.Input,
                    initialDisplayedMonthMillis = null
                )
            }

            Text(
                text = "Create events to connect with your family.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            AppInputField(
                value = eventName,
                onValueChange = { eventName = it },
                placeholderText = "Enter event name",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF03A9F4),
                    unfocusedBorderColor = Color(0xFF03A9F4),
                ),
            )
            AppInputField(
                value = eventDescription,
                onValueChange = { eventDescription = it },
                placeholderText = "Enter event description",
                isResponseError = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF03A9F4),
                    unfocusedBorderColor = Color(0xFF03A9F4),
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

            Log.d("datePickerState", selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE).toString())
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Create Event",
                isLoading = false,
                onClick = {
                    val event = CreateEventRequest(
                        name = eventName,
                        description = eventDescription,
                        eventDate = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE).toString(),
                        familyId = familyId
                    )
                    viewModel.createEvent(event)
                },
                buttonColor = Color(0xFF00BCD4)
            )
        }
    }
}
