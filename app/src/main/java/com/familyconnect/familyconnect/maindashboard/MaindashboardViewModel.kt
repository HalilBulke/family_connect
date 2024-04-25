package com.familyconnect.familyconnect.maindashboard

import androidx.lifecycle.ViewModel
import com.familyconnect.familyconnect.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class DashboardItem(
    val id: Int,
    val title: String,
    val icon: Int, // Icon resource id
    var route: String,
    val permittedScenarios: List<Int> // List of permitted scenarios
)


@HiltViewModel
class MainDashboardViewModel @Inject constructor() : ViewModel() {
    var dashboardItems = listOf(
        DashboardItem(id = 1, title = "TEMP", icon = R.drawable.ic_family_connect, route = "family_members", permittedScenarios = listOf(2, 4, 9)),
        DashboardItem(id = 2, title = "Calendar", icon = R.drawable.ic_family_connect, route = "calendar", permittedScenarios = listOf(2, 3, 4, 9)),
        DashboardItem(id = 3, title = "Create Task", icon = R.drawable.ic_family_connect, route = "createTask", permittedScenarios = listOf(4, 9)),
        DashboardItem(id = 4, title = "Create Family", icon = R.drawable.ic_family_connect, route = "createFamily", permittedScenarios = listOf(3, 9)),
        DashboardItem(id = 5, title = "Spin Wheel", icon = R.drawable.ic_family_connect, route = "spinWheel", permittedScenarios = listOf(2, 4, 9)),
        DashboardItem(id = 6, title = "TEMP", icon = R.drawable.ic_family_connect, route = "daily_activity", permittedScenarios = listOf(2, 4, 9)),
        DashboardItem(id = 7, title = "Show my Tasks", icon = R.drawable.ic_family_connect, route = "getTaskchild", permittedScenarios = listOf(2, 9)),
        DashboardItem(id = 8, title = "TEMP", icon = R.drawable.ic_family_connect, route = "settings", permittedScenarios = listOf(2, 3, 4, 9)),
        DashboardItem(id = 9, title = "My Family", icon = R.drawable.ic_family_connect, route = "displayFamily", permittedScenarios = listOf(2, 4, 9)),
        DashboardItem(id = 10, title = "Add Member", icon = R.drawable.ic_family_connect, route = "addFamilyMember", permittedScenarios = listOf(4, 9)),
        DashboardItem(id = 11, title = "Show my Given Tasks", icon = R.drawable.ic_family_connect, route = "showallgiventasks", permittedScenarios = listOf(4, 9))
    )

    fun filterDashboardItems(role: String, hasFamily: Boolean): List<DashboardItem> {
        val currentScenario = when {
            role == "3" && !hasFamily -> 1
            role == "3" && hasFamily -> 2
            role == "2" && !hasFamily -> 3
            role == "2" && hasFamily -> 4
            role == "1" -> 9
            else -> 0 // Default scenario
        }
        return dashboardItems.filter { it.permittedScenarios.contains(currentScenario) }
    }
}

