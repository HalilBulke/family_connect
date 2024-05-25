package com.familyconnect.familyconnect.familyRewards

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface FamilyRewardsUiState {
    object Loading : FamilyRewardsUiState
    data class Error(val errorMessageTitle: String? = "Rewards Screen Error",val errorMessageDescription: String? = "Error Description") :
        FamilyRewardsUiState
    data class Success(val familyRewardsList: List<Reward>?) : FamilyRewardsUiState
}


@HiltViewModel
class FamilyRewardsViewModel @Inject constructor(
    private val familyRewardsRepository: FamilyRewardsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<FamilyRewardsUiState>(FamilyRewardsUiState.Loading)
    val uiState: StateFlow<FamilyRewardsUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
        Log.d("username", userName)
        loadAllRewards(userName = userName)
    }

    fun retry() {
        Log.d("retry", userName)
        loadAllRewards(userName = userName)
    }

    private fun loadAllRewards(userName: String) {
        viewModelScope.launch {
            try {
                val response = familyRewardsRepository.getFamilyRewards(userName)

                if (response.isSuccessful) {
                    _uiState.value = FamilyRewardsUiState.Success(
                        response.body()
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = FamilyRewardsUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: IOException) {
                _uiState.value = FamilyRewardsUiState.Error()
            }
        }
    }
}
