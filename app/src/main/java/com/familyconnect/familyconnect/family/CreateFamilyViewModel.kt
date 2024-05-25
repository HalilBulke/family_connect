package com.familyconnect.familyconnect.family

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateFamilyUiState {
    object Default : CreateFamilyUiState
    object Loading : CreateFamilyUiState
    data class Error(val errorMessageTitle: String? = "Create Family Error",val errorMessageDescription: String? = "Error Description") :
        CreateFamilyUiState
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
                    _uiState.value = CreateFamilyUiState.Default
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = CreateFamilyUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = CreateFamilyUiState.Error()
            }
        }
    }
}
