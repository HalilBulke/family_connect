package com.familyconnect.familyconnect.taskGetchild

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.showallgiventasks.AllTasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    fun acceptTask(userName: String, taskId: Int) {
        viewModelScope.launch {
            _uiState.value = ChildTasksUiState.Loading
            delay(500)
            try {
                val acceptTask = taskRepository.acceptTask(userName, taskId)
                if (acceptTask.isSuccessful) {
                    getChildTasks(userName)
                } else {
                    _uiState.value = ChildTasksUiState.Error
                }
            } catch (e: Exception) {
                Log.d("catch error", e.toString())
                _uiState.value = ChildTasksUiState.Error
            }
        }
    }

    fun rejectTask(userName: String, taskId: Int) {
        viewModelScope.launch {
            _uiState.value = ChildTasksUiState.Loading
            delay(500)
            try {
                val rejectTask = taskRepository.rejectTask(userName,taskId)
                if (rejectTask.isSuccessful) {
                    getChildTasks(userName)
                } else {
                    _uiState.value = ChildTasksUiState.Error
                }
            } catch (e: Exception) {
                Log.d("catch error", e.toString())
                _uiState.value = ChildTasksUiState.Error
            }
        }
    }
}
