package com.familyconnect.familyconnect.addfamilymember

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.displayfamily.MyFamily
import com.familyconnect.familyconnect.displayfamily.MyFamilyUiState
import com.familyconnect.familyconnect.task.CreateTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AddFamilyMemberUiState {
    object Default : AddFamilyMemberUiState
    object Loading : AddFamilyMemberUiState
    data class Error(val errorMessageTitle: String? = "Add Member Error",val errorMessageDescription: String? = "Error Description") :
        AddFamilyMemberUiState
    object Success : AddFamilyMemberUiState
}

@HiltViewModel
class AddFamilyMemberViewModel @Inject constructor(
    private val addFamilyMemberRepository: AddFamilyMemberRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddFamilyMemberUiState>(AddFamilyMemberUiState.Default)
    val uiState: StateFlow<AddFamilyMemberUiState> = _uiState

    fun retry() {
        Log.d("retry", "retry")
        _uiState.value = AddFamilyMemberUiState.Default
    }

    fun addFamilyMember(request: AddFamilyMemberRequest) {
        viewModelScope.launch {
            _uiState.value = AddFamilyMemberUiState.Loading
            delay(500)
            try {
                val response = addFamilyMemberRepository.addFamilyMember(request)
                Log.d("MEMBER", response.body().toString())
                if (response.isSuccessful) {
                    _uiState.value = AddFamilyMemberUiState.Success
                    delay(500)
                    _uiState.value = AddFamilyMemberUiState.Default
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = AddFamilyMemberUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = AddFamilyMemberUiState.Error()
            }
        }
    }
}
