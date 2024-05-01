package com.familyconnect.familyconnect.taskGetchild

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ChildTasksUiState {
    data class Success(val allTasks: List<Task>?) : ChildTasksUiState
    object Loading : ChildTasksUiState
    object Error : ChildTasksUiState
}

@HiltViewModel
class ChildTasksViewModel @Inject constructor(
    private val taskRepository: GetTasksRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChildTasksUiState>(ChildTasksUiState.Loading)
    val uiState: StateFlow<ChildTasksUiState> = _uiState

    init {
        Log.d("username", userName)
        getChildTasks(userName = userName)
    }

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    private fun getChildTasks(userName: String) {
        viewModelScope.launch {
            try {
                val getAllTasksResponse = taskRepository.getTasksByUsername(userName)
                if (getAllTasksResponse.isSuccessful) {
                    _uiState.value = ChildTasksUiState.Success(
                        getAllTasksResponse.body()
                    )
                } else {
                    _uiState.value = ChildTasksUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = ChildTasksUiState.Error
            }
        }
    }

    /*
    fun completeTask(username: String, taskId: Int) {
        viewModelScope.launch {
            try {

                Log.d("completeTask", "Completing task with taskId: $taskId")
                val response = taskRepository.setTaskPending(username, taskId)
                if (response.isSuccessful) {
                    // Handle successful task completion, maybe update the UI or task list
                    fetchTasks(username)  // Refresh the list or handle UI update
                } else {
                    _errorMessage.value = "Failed to complete task: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

     */
}
