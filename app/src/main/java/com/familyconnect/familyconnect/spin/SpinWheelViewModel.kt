package com.familyconnect.familyconnect.spin

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.createprogress.CreateProgressApiService
import com.familyconnect.familyconnect.createprogress.CreateProgressRequestBody
import com.familyconnect.familyconnect.createprogress.CreateProgressUiState
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import com.familyconnect.familyconnect.displayfamily.FamilySpinDataDTO
import com.familyconnect.familyconnect.displayfamily.SpinWheel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SpinWheelUiState {
    data class Success(val spinList: List<SpinWheel>) : SpinWheelUiState
    object Loading : SpinWheelUiState
    data class Error(val errorMessageTitle: String? = "Spin Wheel Screen Error",val errorMessageDescription: String? = "Error Description") :
        SpinWheelUiState
}


@HiltViewModel
class SpinWheelViewModel @Inject constructor(
    private val displayFamilyRepository: DisplayFamilyRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SpinWheelUiState>(SpinWheelUiState.Loading)
    val uiState: StateFlow<SpinWheelUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    private val _spinWheels = MutableStateFlow<List<SpinWheel>>(listOf())

    private val _selectedItem = MutableStateFlow("")

    init {
        Log.d("username", userName)
        loadSpinWheels(userName = userName)
    }

    private fun loadSpinWheels(userName: String) {
        viewModelScope.launch {
            _uiState.value = SpinWheelUiState.Loading
            try {
                val response = displayFamilyRepository.getSpinWheels(userName)
                Log.d("FAM", response.toString())
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        _uiState.value = SpinWheelUiState.Success(
                            spinList = responseData,
                        )
                        _spinWheels.value = responseData
                    } else {
                        _uiState.value = SpinWheelUiState.Error()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = SpinWheelUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = SpinWheelUiState.Error()
            }
        }
    }

    fun setReward(familySpinDataDTO: FamilySpinDataDTO) {
        viewModelScope.launch {
            try {
                _uiState.value = SpinWheelUiState.Loading
                delay(500)
                val response = displayFamilyRepository.setReward(familySpinDataDTO)
                if (response.isSuccessful) {
                    _uiState.value = SpinWheelUiState.Success(_spinWheels.value)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = SpinWheelUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = SpinWheelUiState.Error()
            }
        }
    }
}