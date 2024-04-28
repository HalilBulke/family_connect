package com.familyconnect.familyconnect.displayfamily

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MyFamilyScreen(
    username: String?,
    viewModel: MyFamilyViewModel = hiltViewModel()
) {
    val familyData by viewModel.familyData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()
    var isExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.fetchFamily(username.toString())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading == true) {
            // Display loading indicator
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        } else {
            if (!errorMessage.isNullOrEmpty()) {
                // Display error message if available
                Text(text = "Error: $errorMessage")
            } else {
                // Display family data if available
                familyData?.let { family ->
                    Text(
                        text = "Family Name:",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = family.familyName,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Family Members:",
                            style = TextStyle(fontSize = 32.sp),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Expand/Collapse"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isExpanded) {
                        family.familyMembers.forEach { member ->
                            Text(
                                text = "- $member",
                                style = TextStyle(fontSize = 24.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}
