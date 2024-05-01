package com.familyconnect.familyconnect.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.calendar.CalenderUiState
import com.familyconnect.familyconnect.login.User
import com.familyconnect.familyconnect.register.RegisterUiState
import com.familyconnect.familyconnect.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    object Error : ProfileUiState
    data class Success(val userInfo: User) : ProfileUiState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
        Log.d("username", userName)
        fetchUserData(username = userName)
    }

    fun fetchUserData(username: String) {
        viewModelScope.launch {
            try {
                // Fetch user data using the repository
                val response = profileRepository.getUser(username)
                if (response.isSuccessful) {
                    // Update the LiveData with the user data
                    if (response.body() != null) {
                        _uiState.value = ProfileUiState.Success(response.body()!!)
                    }
                    else {
                        _uiState.value = ProfileUiState.Error
                    }

                } else {
                    _uiState.value = ProfileUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error
            }
        }
    }

    fun updateName(username: String, newName: String,  onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = profileRepository.updateName(username, newName)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError(response.message())
                }
            } catch (e: Exception) {
                onError("Unexpected Error")
            }
        }
    }

    fun updatePassword(username: String, oldPassword: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = profileRepository.updatePassword(username, oldPassword, newPassword)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    Log.d( "response", response.message().toString())
                    onError(response.message() ?: "Failed to update password")
                }
            } catch (e: Exception) {
                onError("Unexpected Error")
            }
        }
    }
}
