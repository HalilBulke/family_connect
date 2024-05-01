package com.familyconnect.familyconnect.task

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateTaskUiState {
    object Loading : CreateTaskUiState
    object Error : CreateTaskUiState
    data class Success(val familyMembers: List<String>?) : CreateTaskUiState
}

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val createTaskApiService: CreateTaskApiService,
    private val displayFamilyRepository: DisplayFamilyRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateTaskUiState>(CreateTaskUiState.Loading)
    val uiState: StateFlow<CreateTaskUiState> = _uiState

    private val _familyMembers = MutableStateFlow<List<String>>(listOf())

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
        Log.d("username", userName)
        loadFamilyMembers(userName = userName)
    }

    fun retry() {
        Log.d("retry", userName)
        loadFamilyMembers(userName = userName)
    }

    fun addTask(task: CreateTaskRequestBody) {
        viewModelScope.launch {
            _uiState.value = CreateTaskUiState.Loading
            try {
                val response = createTaskApiService.addTask(task)
                if (response.isSuccessful) {
                    _uiState.value = CreateTaskUiState.Success(familyMembers = _familyMembers.value)
                } else {
                    _uiState.value = CreateTaskUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = CreateTaskUiState.Error
            }
        }
    }

    private fun loadFamilyMembers(userName: String) {
        viewModelScope.launch {
            try {
                val response = displayFamilyRepository.getFamily(userName)
                Log.d("FAM", response.toString())
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        _uiState.value = CreateTaskUiState.Success(
                            familyMembers = responseData.familyMembers
                        )
                        _familyMembers.value = responseData.familyMembers
                    } else {
                        _uiState.value = CreateTaskUiState.Error
                    }
                } else {
                    _uiState.value = CreateTaskUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = CreateTaskUiState.Error
            }
        }
    }
}
