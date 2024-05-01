package com.familyconnect.familyconnect.maindashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.login.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardItem(
    val id: Int,
    val title: String,
    val icon: Int, // Icon resource id
    var route: String,
    val permittedScenarios: List<Int>, // List of permitted scenarios
    val color: Color
)


@HiltViewModel
class MainDashboardViewModel @Inject constructor(
    private val mainDashboardRepository: MainDashboardRepository
) : ViewModel() {

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    var dashboardItems = listOf(
        DashboardItem(id = 1, title = "Calendar", icon = R.drawable.calendar, route = "calendar", permittedScenarios = listOf(2, 3, 4, 9),
            color = Color(0xFFFFFFFF)
        ),
        DashboardItem(id = 2, title = "Create Task", icon = R.drawable.create_task, route = "createTask", permittedScenarios = listOf(4, 9),
            color = Color(0xFF03A9F4)),
        DashboardItem(id = 3, title = "Create Family", icon = R.drawable.family, route = "createFamily", permittedScenarios = listOf(3, 9),
            color = Color(0xFFA47CEC)),
        DashboardItem(id = 4, title = "Spin Wheel", icon = R.drawable.spin_wheel_icon, route = "spinWheel", permittedScenarios = listOf(2, 4, 9),
            color = Color(0xFFFF5722)
        ),
        DashboardItem(id = 5, title = "Show my Tasks", icon = R.drawable.display_tasks, route = "getTaskchild", permittedScenarios = listOf(2, 9),
            color = Color(0xFF009688)
        ),
        DashboardItem(id = 6, title = "My Family", icon = R.drawable.family, route = "displayFamily", permittedScenarios = listOf(2, 4, 9),
            color = Color(0xFFA47CEC)),
        DashboardItem(id = 7, title = "Add Member", icon = R.drawable.add_member1, route = "addFamilyMember", permittedScenarios = listOf(4, 9),
            color = Color(0xFFFFC107)),
        DashboardItem(id = 8, title = "Given Tasks", icon = R.drawable.display_tasks, route = "showallgiventasks", permittedScenarios = listOf(4, 9),
            color = Color(0xFF009688)),
        DashboardItem(id = 9, title = "Create Progress", icon = R.drawable.progress, route = "createProgress", permittedScenarios = listOf(4, 9),
            color = Color(0xFFE0724F)),
        DashboardItem(id = 10, title = "Show my Progresses", icon = R.drawable.progress, route = "getProgresschild", permittedScenarios = listOf(2 , 9),
            color = Color(0xFF00BCD4)
        ),
        DashboardItem(id = 11, title = "Given Progresses", icon = R.drawable.progress, route = "showallgivenprogress", permittedScenarios = listOf(4, 9),
            color = Color(0xFFE0FFFF)),
        DashboardItem(id = 12, title = "Create Event", icon = R.drawable.event, route = "createEvent", permittedScenarios = listOf(4, 9),
            color = Color(0xFF8BC34A)),
    )

    val profileDashboardItem = DashboardItem(id = 12, title = "Profile", icon = R.drawable.profile, route = "profile", permittedScenarios = listOf(2,3,4,9),
        color = Color(0xFFE0FFFF))

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

    fun fetchUserData(username: String) {
        viewModelScope.launch {
            try {
                // Fetch user data using the repository
                val response = mainDashboardRepository.getUser(username)
                if (response.isSuccessful) {
                    // Update the LiveData with the user data
                    _userData.value = response.body()
                } else {
                    // Handle error response
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}

