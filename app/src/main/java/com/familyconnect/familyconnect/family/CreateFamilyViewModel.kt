package com.familyconnect.familyconnect.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateFamilyUiState {
    object Default : CreateFamilyUiState
    object Loading : CreateFamilyUiState
    object Error : CreateFamilyUiState
    object Success : CreateFamilyUiState
}

@HiltViewModel
class CreateFamilyViewModel @Inject constructor(
    private val familyRepository: NetworkFamilyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateFamilyUiState>(CreateFamilyUiState.Default)
    val uiState: StateFlow<CreateFamilyUiState> = _uiState

    fun createFamily(family: CreateFamilyRequestBody) {
        viewModelScope.launch {
            _uiState.value = CreateFamilyUiState.Loading
            delay(500)
            try {
                val response = familyRepository.createFamily(family)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = CreateFamilyUiState.Success
                    delay(500)
                } else {
                    _uiState.value = CreateFamilyUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = CreateFamilyUiState.Error
            } finally {
                _uiState.value = CreateFamilyUiState.Default
            }
        }
    }
}
