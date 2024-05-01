package com.familyconnect.familyconnect.showallgiventasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.EmptyTaskComponent
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.ItemCard
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.taskGetchild.Task

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllTasksScreen(
    viewModel: AllTasksViewModel = hiltViewModel(),
    userName : String?,
    onOkButtonClicked: () -> Unit,
    onAcceptButtonClicked: (String, Int) -> Unit,
    onRejectButtonClicked: (String, Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is AllTasksUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onOkButtonClicked() },
            )
        }
        is AllTasksUiState.Loading -> {
            LoadingScreen()
        }
        is AllTasksUiState.Success -> {
            AllTasksScreenPage(
                allTasks = (uiState as AllTasksUiState.Success).allTasks,
                onOkButtonClicked = onOkButtonClicked,
                onAcceptButtonClicked = onAcceptButtonClicked,
                onRejectButtonClicked = onRejectButtonClicked,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AllTasksScreenPage(
    allTasks: List<Task>?,
    onOkButtonClicked: () -> Unit,
    onAcceptButtonClicked: (String, Int) -> Unit,
    onRejectButtonClicked: (String, Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pageColor = Color(0xFF009688)

    Column {
        TopAppBar(
            title = { Text(text = "All Tasks") },
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
            Modifier.padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.all_tasks),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }
        if (allTasks.isNullOrEmpty()) {
            EmptyTaskComponent()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            )
            {
                itemsIndexed(items = allTasks) { index, task ->
                    Box(
                        modifier = Modifier.animateItemPlacement(tween(500))
                    ) {
                        ItemCard(modifier = Modifier.padding(4.dp)) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Task Name: ${task.taskName}",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Description: ${task.taskDescription}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Creator: ${task.taskCreatorUserName}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Assignee: ${task.taskAssigneeUserName}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Due Date: ${task.taskDueDate.take(10)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Reward Points: ${task.taskRewardPoints}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Status: ${task.status}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(
                                            0xFF4CAF50
                                        )
                                        ),
                                        onClick = { onAcceptButtonClicked(task.taskAssigneeUserName,task.taskId) },
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Text(text = "Approve")
                                    }
                                    Button(
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(
                                            0xFFF44336
                                        )
                                        ),
                                        onClick = { onRejectButtonClicked(task.taskAssigneeUserName,task.taskId) }
                                    ) {
                                        Text(text = "Reject")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}