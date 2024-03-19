package com.familyconnect.familyconnect.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    Scaffold(
            bottomBar = { BottomNavigationBar() }
    ) { padding ->
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
        ) {
            TopBar(name = "Dan Smith")
            Spacer(modifier = Modifier.height(16.dp))
            WeeklyTasksSection(weeklyTasks = listOf("Task1", "Task2", "Task3")) // Sample tasks
            Spacer(modifier = Modifier.height(16.dp))
            TodaysTasksSection(todaysTasks = listOf("Task1", "Task2")) // Sample tasks
        }
    }
}

@Composable
fun TopBar(name: String) {
    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Profile image (placeholder)
        Image(
                painter = painterResource(id = R.drawable.ic_family_connect),
                contentDescription = stringResource(id = R.string.login_header),
                modifier = Modifier.size(48.dp)
        )
        // Greeting
        Text(text = "Good Evening!\n$name", fontWeight = FontWeight.Bold)
        // Search and notification icons (placeholders)
        Icon(Icons.Filled.Search, contentDescription = "Search")
        Icon(Icons.Filled.Home, contentDescription = "Notifications")
    }
}

@Composable
fun WeeklyTasksSection(weeklyTasks: List<String>) {
    // This section will be a horizontal scrolling list
    LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weeklyTasks) { task ->
            TaskCard(taskName = task) // You will define this composable to display the task details
        }
    }
}

@Composable
fun TaskCard(taskName: String) {
    // Placeholder for the task card UI
    Card(
            modifier = Modifier.width(200.dp).height(100.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center
        ) {
            Text(text = taskName)
        }
    }
}

@Composable
fun TodaysTasksSection(todaysTasks: List<String>) {
    // This section will be a vertical scrolling list
    LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(todaysTasks) { task ->
            TaskCard(taskName = task) // Reuse the TaskCard composable
        }
    }
}


@Composable
fun BottomNavigationBar() {
    // Define the background color for the Bottom Bar
    val backgroundColor = MaterialTheme.colorScheme.primary

    // Custom bottom bar container
    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround, // Distribute the icons evenly
            verticalAlignment = Alignment.CenterVertically // Center the icons vertically
    ) {
        // Replace these with your actual icons and navigation logic
        BottomBarIcon(placeHolderIcon(), "Home")
        BottomBarIcon(placeHolderIcon(), "Search")
        BottomBarIcon(placeHolderIcon(), "Calendar")
        BottomBarIcon(placeHolderIcon(), "Settings")
    }
}

@Composable
fun BottomBarIcon(
        imageVector: ImageVector,
        contentDescription: String
) {
    Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary // Use the onPrimary color for icon tint
    )
}

// Placeholder function to create a placeholder icon
@Composable
fun placeHolderIcon(): ImageVector {
    // You can use any default icon here
    // Alternatively, you can draw something custom or return a basic shape
    return Icons.Default.Home // Placeholder icon
}







@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}
