package com.familyconnect.familyconnect.allProgress

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.calendar.CalenderUiState
import com.familyconnect.familyconnect.progressGetChild.Progress
import com.familyconnect.familyconnect.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface AllProgressUiState {
    object Loading : AllProgressUiState
    object Error : AllProgressUiState
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
                    _uiState.value = AllProgressUiState.Error
                }
            } catch (e: IOException) {
                _uiState.value = AllProgressUiState.Error
            }
        }
    }
}
