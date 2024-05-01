package com.familyconnect.familyconnect.createprogress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import com.familyconnect.familyconnect.displayfamily.Family
import com.familyconnect.familyconnect.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateProgressUiState {
    data class Default(val familyMembers: List<String>) : CreateProgressUiState
    object Loading : CreateProgressUiState
    object Error : CreateProgressUiState
    object Success : CreateProgressUiState
}

@HiltViewModel
class CreateProgressViewModel @Inject constructor(
    private val createProgressApiService: CreateProgressApiService,
    private val displayFamilyRepository: DisplayFamilyRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateProgressUiState>(CreateProgressUiState.Loading)
    val uiState: StateFlow<CreateProgressUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    private val _familyMembers = MutableStateFlow<List<String>>(listOf())

    init {
        Log.d("username", userName)
        loadFamilyMembers(userName = userName)
    }

    fun addProgress(progress: CreateProgressRequestBody) {
        viewModelScope.launch {
            try {
                _uiState.value = CreateProgressUiState.Loading
                delay(500)
                val response = createProgressApiService.addProgress(progress)
                if (response.isSuccessful) {
                    _uiState.value = CreateProgressUiState.Success
                    delay(500)
                } else {
                    _uiState.value = CreateProgressUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = CreateProgressUiState.Error
            } finally {
                _uiState.value = CreateProgressUiState.Default(
                    familyMembers = _familyMembers.value,
                )
            }
        }
    }

    fun loadFamilyMembers(userName: String) {
        viewModelScope.launch {
            _uiState.value = CreateProgressUiState.Loading
            try {
                val response = displayFamilyRepository.getFamily(userName)
                Log.d("FAM", response.toString())
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        _uiState.value = CreateProgressUiState.Default(
                            familyMembers = responseData.familyMembers,
                        )
                        _familyMembers.value = responseData.familyMembers
                    } else {
                        _uiState.value = CreateProgressUiState.Error
                    }
                } else {
                    _uiState.value = CreateProgressUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = CreateProgressUiState.Error
            }
        }
    }
}