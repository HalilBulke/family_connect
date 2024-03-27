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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate

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
            TopBar(name = "Halil")
            Spacer(modifier = Modifier.height(4.dp))
            CalendarSection()
            Spacer(modifier = Modifier.height(6.dp))
            TasksSection(Tasks = listOf("Task1", "Task2", "Task3", "Task4", "Task5", "Task6", "Task7")) // Sample tasks
        }
    }
}

@Composable
fun CalendarSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CalendarHeader()
        CalendarBody()
    }
}

@Composable
fun CalendarHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "March 2024", // Display the current month and year
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Today: ${LocalDate.now().dayOfMonth}", // Display today's date
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun CalendarBody() {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val daysInMonth = 31 // Replace this with the actual number of days in the month
    val startDate = LocalDate.now().withDayOfMonth(1) // Start from the 1st day of the month

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Weekdays
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (day in daysOfWeek) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
            }
        }

        // Days
        val numRows = (daysInMonth + daysOfWeek.size - 1) / daysOfWeek.size
        for (i in 0 until numRows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (j in daysOfWeek.indices) {
                    val dayIndex = i * daysOfWeek.size + j
                    if (dayIndex < daysInMonth) {
                        val date = startDate.plusDays(dayIndex.toLong())
                        val isToday = LocalDate.now().isEqual(date)
                        DayItem(
                            day = date.dayOfMonth.toString(),
                            isToday = isToday,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DayItem(
    day: String,
    isToday: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape
) {
    val backgroundColor = if (isToday) Color.Red else Color.Transparent
    val contentColor = if (isToday) Color.White else Color.Black

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = shape,
        border = BorderStroke(2.dp, Color.Black),
        modifier = modifier.size(20.dp),
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize()
        )
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
        Text(text = "Good Evening $name!", fontWeight = FontWeight.Bold)
        // Search and notification icons (placeholders)
        Icon(Icons.Filled.Search, contentDescription = "Search")
        Icon(Icons.Filled.Home, contentDescription = "Notifications")
    }
}

@Composable
fun TaskCard(taskName: String) {
    // Placeholder for the task card UI
    Card(
            modifier = Modifier.width(350.dp).height(100.dp),
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
fun TasksSection(Tasks: List<String>) {
    // This section will be a vertical scrolling list
    LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(Tasks) { task ->
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
        BottomBarIcon(Icons.Default.Home, "Home")
        BottomBarIcon(Icons.Default.Face, "Family")
        BottomBarIcon(Icons.Default.DateRange, "Calendar")
        BottomBarIcon(Icons.Default.Settings, "Settings")
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

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}
