package com.familyconnect.familyconnect.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Task(
        val id: Int,
        val title: String,
        val description: String,
        val isCompleted: Boolean,
        // Add any other properties that are relevant to your task model
)

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    // Assuming these would be fetched from a repository or database
    private val _weeklyTasks = MutableStateFlow<List<Task>>(emptyList())
    val weeklyTasks: StateFlow<List<Task>> = _weeklyTasks

    private val _todaysTasks = MutableStateFlow<List<Task>>(emptyList())
    val todaysTasks: StateFlow<List<Task>> = _todaysTasks

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            // Simulate loading tasks with delay or async operation
            // For now, we'll just create some mock tasks

            _weeklyTasks.value = listOf(
                    Task(id = 1, title = "Create a Landing Page", description = "UI/UX Design", isCompleted = false),
                    Task(id = 2, title = "Develop a Website", description = "Development", isCompleted = false),
                    // Add more tasks as needed...
            )

            _todaysTasks.value = listOf(
                    Task(id = 1, title = "Design 2 App Screens", description = "Crypto Wallet App", isCompleted = true),
                    // Add more tasks as needed...
            )
        }
    }
}
