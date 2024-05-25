package com.familyconnect.familyconnect.childRewards

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.familyRewards.FamilyRewardsRepository
import com.familyconnect.familyconnect.familyRewards.Reward
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface ChildRewardsUiState {
    object Loading : ChildRewardsUiState
    data class Error(val errorMessageTitle: String? = "Rewards Screen Error",val errorMessageDescription: String? = "Error Description") :
        ChildRewardsUiState
    data class Success(val childRewardsList: List<Reward>?) : ChildRewardsUiState
}


@HiltViewModel
class ChildRewardsViewModel @Inject constructor(
    private val familyRewardsRepository: FamilyRewardsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ChildRewardsUiState>(ChildRewardsUiState.Loading)
    val uiState: StateFlow<ChildRewardsUiState> = _uiState

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
                val response = familyRewardsRepository.getUserRewards(userName)

                if (response.isSuccessful) {
                    _uiState.value = ChildRewardsUiState.Success(
                        response.body()
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = ChildRewardsUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: IOException) {
                _uiState.value = ChildRewardsUiState.Error()
            }
        }
    }
}
