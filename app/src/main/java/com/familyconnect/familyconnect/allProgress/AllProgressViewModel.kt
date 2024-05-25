package com.familyconnect.familyconnect.allProgress

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.calendar.CalenderUiState
import com.familyconnect.familyconnect.progressGetChild.Progress
import com.familyconnect.familyconnect.showallgiventasks.AllTasksUiState
import com.familyconnect.familyconnect.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface AllProgressUiState {
    object Loading : AllProgressUiState
    data class Error(val errorMessageTitle: String? = "All Progresses Error",val errorMessageDescription: String? = "Error Description") :
        AllProgressUiState
    data class Success(val allProgressList: List<Progress>?) : AllProgressUiState
}


@HiltViewModel
class AllProgressViewModel @Inject constructor(
    private val allProgressRepository: AllProgressRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AllProgressUiState>(AllProgressUiState.Loading)
    val uiState: StateFlow<AllProgressUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
        Log.d("username", userName)
        loadAllProgress(userName = userName)
    }

    fun retry() {
        Log.d("retry", userName)
        loadAllProgress(userName = userName)
    }

    private fun loadAllProgress(userName: String) {
        viewModelScope.launch {
            try {
                val response = allProgressRepository.getAllProgress(userName)

                if (response.isSuccessful) {
                    _uiState.value = AllProgressUiState.Success(
                        response.body()
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = AllProgressUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: IOException) {
                _uiState.value = AllProgressUiState.Error()
            }
        }
    }

    fun completeProgress(userName: String, progressId: Int) {
        viewModelScope.launch {
            _uiState.value = AllProgressUiState.Loading
            delay(500)
            try {
                val acceptTask = allProgressRepository.completeProgress(userName, progressId)
                if (acceptTask.isSuccessful) {
                    loadAllProgress(userName)
                } else {
                    Log.d("parent taskı accept", acceptTask.body().toString())
                    val errorBody = acceptTask.errorBody()?.string()
                    _uiState.value = AllProgressUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.d("parent taskı accept", e.toString())
                _uiState.value = AllProgressUiState.Error()
            }
        }
    }

    fun cancelProgress(userName: String, progressId: Int) {
        viewModelScope.launch {
            _uiState.value = AllProgressUiState.Loading
            delay(500)
            try {
                val rejectTask = allProgressRepository.cancelProgress(userName,progressId)
                if (rejectTask.isSuccessful) {
                    loadAllProgress(userName)
                } else {
                    val errorBody = rejectTask.errorBody()?.string()
                    _uiState.value = AllProgressUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                    Log.d("parent progress reject", rejectTask.toString())

                }
            } catch (e: Exception) {
                Log.d("catch error", e.toString())
                _uiState.value = AllProgressUiState.Error()
            }
        }
    }
}
