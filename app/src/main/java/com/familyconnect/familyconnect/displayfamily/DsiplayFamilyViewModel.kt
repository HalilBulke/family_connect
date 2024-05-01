package com.familyconnect.familyconnect.displayfamily

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MyFamilyUiState {
    object Loading : MyFamilyUiState
    object Error : MyFamilyUiState
    data class Success(val family: MyFamily) : MyFamilyUiState
}

@HiltViewModel
class MyFamilyViewModel @Inject constructor(
    private val familyRepository: DisplayFamilyRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyFamilyUiState>(MyFamilyUiState.Loading)
    val uiState: StateFlow<MyFamilyUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
        Log.d("username", userName)
        fetchFamily(userName = userName)
    }

    fun retry() {
        Log.d("retry", userName)
        fetchFamily(userName = userName)
    }

    private fun fetchFamily(userName: String) {
        viewModelScope.launch {
            try {
                val familyResponse = familyRepository.getFamily(userName)
                val familyMembersResponse = familyRepository.getFamilyMembers(userName)

                Log.d("FAM", familyResponse.toString())
                if (familyResponse.isSuccessful && familyMembersResponse.isSuccessful) {
                    val familyResponseData = familyResponse.body()
                    val familyMembersResponseData = familyMembersResponse.body()
                    if (familyResponseData != null && familyMembersResponseData != null) {
                        _uiState.value = MyFamilyUiState.Success(MyFamily(
                            id = familyResponseData.id,
                            familyName = familyResponseData.familyName,
                            familyMembers = familyMembersResponseData,
                            creatorUserName = familyResponseData.creatorUserName
                        ))
                    } else {
                        _uiState.value = MyFamilyUiState.Error
                    }
                } else {
                    _uiState.value = MyFamilyUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = MyFamilyUiState.Error
            }
        }
    }
}

data class MyFamily(
    val id: Int, // Change the type according to the actual type in the response
    val familyName: String,
    val familyMembers: List<FamilyMembers>, // Change the type according to the actual type in the response
    val creatorUserName: String
)