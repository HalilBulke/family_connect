package com.familyconnect.familyconnect.progressGetChild


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.allProgress.AllProgressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface ChildProgressUiState {
    object Loading : ChildProgressUiState
    data class Error(val errorMessageTitle: String? = "All Progresses Error",val errorMessageDescription: String? = "Error Description") :
        ChildProgressUiState
    data class Success(val allProgressList: List<Progress>?) : ChildProgressUiState
}

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getProgressRepository: GetProgressRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ChildProgressUiState>(ChildProgressUiState.Loading)
    val uiState: StateFlow<ChildProgressUiState> = _uiState

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
                val response = getProgressRepository.getProgressByUsername(userName)

                if (response.isSuccessful) {
                    _uiState.value = ChildProgressUiState.Success(
                        response.body()
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = ChildProgressUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: IOException) {
                _uiState.value = ChildProgressUiState.Error()
            }
        }
    }
}
