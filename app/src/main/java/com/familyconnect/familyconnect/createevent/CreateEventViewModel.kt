package com.familyconnect.familyconnect.createevent

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import com.familyconnect.familyconnect.task.CreateTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateEventUiState {
    object Default : CreateEventUiState
    object Loading : CreateEventUiState
    data class Error(val errorMessageTitle: String? = "Add Event Error",val errorMessageDescription: String? = "Error Description") :
        CreateEventUiState
    object Success : CreateEventUiState
}

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val createEventRepository: CreateEventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateEventUiState>(CreateEventUiState.Default)
    var uiState: StateFlow<CreateEventUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    fun retry() {
        Log.d("retry", userName)
    }

    fun createEvent(request: CreateEventRequest) {
        viewModelScope.launch {
            _uiState.value = CreateEventUiState.Loading
            try {
                val response = createEventRepository.createEvent(request)
                if (response.isSuccessful) {
                    _uiState.value = CreateEventUiState.Success
                    delay(500)
                    _uiState.value = CreateEventUiState.Default
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = CreateEventUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = CreateEventUiState.Error()
            }
        }
    }
}

