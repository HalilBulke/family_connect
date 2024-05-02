package com.familyconnect.familyconnect.showallgiventasks


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.task.TaskRepository
import com.familyconnect.familyconnect.taskGetchild.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AllTasksUiState {
    data class Success(val allTasks: List<Task>?) : AllTasksUiState
    object Loading : AllTasksUiState
    object Error : AllTasksUiState
}

@HiltViewModel
class AllTasksViewModel @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllTasksUiState>(AllTasksUiState.Loading)
    val uiState: StateFlow<AllTasksUiState> = _uiState

    init {
        Log.d("username", userName)
        getAllTasks(userName = userName)
    }

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    private fun getAllTasks(userName: String) {
        viewModelScope.launch {
            try {
                val getAllTasksResponse = tasksRepository.getAllTasks(userName)
                if (getAllTasksResponse.isSuccessful) {
                    _uiState.value = AllTasksUiState.Success(
                        getAllTasksResponse.body()
                    )
                } else {
                    _uiState.value = AllTasksUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = AllTasksUiState.Error
            }
        }
    }

    fun acceptTask(userName: String, taskId: Int) {
        viewModelScope.launch {
            _uiState.value = AllTasksUiState.Loading
            delay(500)
            try {
                val acceptTask = tasksRepository.acceptTask(userName, taskId)
                if (acceptTask.isSuccessful) {
                    getAllTasks(userName)
                } else {
                    Log.d("parent taskı accept", acceptTask.body().toString())
                    _uiState.value = AllTasksUiState.Error
                }
            } catch (e: Exception) {
                Log.d("parent taskı accept", e.toString())
                _uiState.value = AllTasksUiState.Error
            }
        }
    }

    fun rejectTask(userName: String, taskId: Int) {
        viewModelScope.launch {
            _uiState.value = AllTasksUiState.Loading
            delay(500)
            try {
                val rejectTask = tasksRepository.rejectTask(userName,taskId)
                if (rejectTask.isSuccessful) {
                    getAllTasks(userName)
                } else {
                    _uiState.value = AllTasksUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = AllTasksUiState.Error
            }
        }
    }
}
