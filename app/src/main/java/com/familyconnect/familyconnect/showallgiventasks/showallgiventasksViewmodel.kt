package com.familyconnect.familyconnect.showallgiventasks


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.task.Task
import com.familyconnect.familyconnect.task.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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


/*
    fun completeTask(userName: String, taskId: Int) {
        taskApiService.completeTask(userName, taskId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle successful task completion, e.g., refresh data
                    Log.d("ViewModel", "Task completed successfully")
                } else {
                    // Handle errors
                    Log.e("ViewModel", "Failed to complete task")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network failure
                Log.e("ViewModel", "Network error on task completion", t)
            }
        })
    }

    fun rejectTask(userName: String, taskId: Int) {
        taskApiService.rejectTask(userName, taskId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle successful task rejection
                    Log.d("ViewModel", "Task rejected successfully")
                } else {
                    // Handle errors
                    Log.e("ViewModel", "Failed to reject task")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle network failure
                Log.e("ViewModel", "Network error on task rejection", t)
            }
        })
    }


 */




}
