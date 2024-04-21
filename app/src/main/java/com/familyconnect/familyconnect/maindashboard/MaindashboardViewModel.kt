package com.familyconnect.familyconnect.maindashboard

import androidx.lifecycle.ViewModel
import com.familyconnect.familyconnect.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class DashboardItem(
    val id: Int,
    val title: String,
    val icon: Int, // Icon resource id
    val route: String
)

@HiltViewModel
class MainDashboardViewModel @Inject constructor() : ViewModel() {
    // List of dashboard items, replace the icons with the actual drawable resource IDs
    val dashboardItems = listOf(
        DashboardItem(id = 1, title = "Family Members", icon = R.drawable.ic_family_connect, route = "family_members"),
        DashboardItem(id = 2, title = "Calendar", icon = R.drawable.ic_family_connect, route = "calendar"),
        DashboardItem(id = 3, title = "Create Task", icon = R.drawable.ic_family_connect, route = "createTask"),
        DashboardItem(id = 4, title = "Budget", icon = R.drawable.ic_family_connect, route = "budget"),


        DashboardItem(id = 5, title = "Spin Wheel", icon = R.drawable.ic_family_connect, route = "spinWheel"),
        DashboardItem(id = 6, title = "Calendar", icon = R.drawable.ic_family_connect, route = "daily_activity"),
        DashboardItem(id = 7, title = "Lists", icon = R.drawable.ic_family_connect, route = "rewards"),
        DashboardItem(id = 8, title = "Budget", icon = R.drawable.ic_family_connect, route = "settings"),

        // ... add other dashboard items
    )
}