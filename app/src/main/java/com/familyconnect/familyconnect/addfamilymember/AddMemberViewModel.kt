package com.familyconnect.familyconnect.addfamilymember

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.displayfamily.MyFamily
import com.familyconnect.familyconnect.displayfamily.MyFamilyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AddFamilyMemberUiState {
    object Default : AddFamilyMemberUiState
    object Loading : AddFamilyMemberUiState
    object Error : AddFamilyMemberUiState
    object Success : AddFamilyMemberUiState
}

@HiltViewModel
class AddFamilyMemberViewModel @Inject constructor(
    private val addFamilyMemberRepository: AddFamilyMemberRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddFamilyMemberUiState>(AddFamilyMemberUiState.Default)
    val uiState: StateFlow<AddFamilyMemberUiState> = _uiState

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
                } else {
                    _uiState.value = AddFamilyMemberUiState.Error
                    delay(500)
                }
            } catch (e: Exception) {
                _uiState.value = AddFamilyMemberUiState.Error
                delay(500)
            } finally {
                _uiState.value = AddFamilyMemberUiState.Default
            }
        }
    }
}
