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
import com.familyconnect.familyconnect.login.User
import com.familyconnect.familyconnect.maindashboard.MainDashboardRepository
import com.familyconnect.familyconnect.task.CreateTaskUiState
import com.familyconnect.familyconnect.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateProgressUiState {
    data class Default(val familyMembers: List<String>, val childNames: List<String>?, val childUserNames: List<String>?) : CreateProgressUiState
    object Loading : CreateProgressUiState
    data class Error(val errorMessageTitle: String? = "Create Progress Error",val errorMessageDescription: String? = "Error Description") :
        CreateProgressUiState
    object Success : CreateProgressUiState
}

@HiltViewModel
class CreateProgressViewModel @Inject constructor(
    private val createProgressApiService: CreateProgressApiService,
    private val displayFamilyRepository: DisplayFamilyRepository,
    private val mainDashboardRepository: MainDashboardRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateProgressUiState>(CreateProgressUiState.Loading)
    val uiState: StateFlow<CreateProgressUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    private val _familyMembers = MutableStateFlow<List<String>>(listOf())

    private val _childNames = MutableStateFlow<List<String>>(emptyList())
    private val _childUserNames = MutableStateFlow<List<String>>(emptyList())

    private val _userData = MutableLiveData<User>()

    init {
        Log.d("username", userName)
        loadFamilyMembers(userName = userName)
    }

    fun retry() {
        Log.d("retry", "retry")
        _uiState.value = CreateProgressUiState.Default(
            familyMembers = _familyMembers.value,
            childNames = _childNames.value,
            childUserNames = _childUserNames.value
        )
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
                    _uiState.value = CreateProgressUiState.Default(
                        familyMembers = _familyMembers.value,
                        childNames = _childNames.value,
                        childUserNames = _childUserNames.value
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = CreateProgressUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = CreateProgressUiState.Error()
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
                        val names = mutableListOf<String>()
                        val usernames = mutableListOf<String>()
                        responseData.familyMembers.forEach { username ->
                            try {
                                val newResponse = mainDashboardRepository.getUser(username)
                                if (newResponse.isSuccessful) {
                                    _userData.value = newResponse.body()
                                    if (_userData.value != null && _userData.value!!.authorities.any { it.roleId == 3 }) {
                                        names.add(_userData.value!!.name)
                                        usernames.add(username)
                                    }
                                } else {
                                    val errorBody = newResponse.errorBody()?.string()
                                    _uiState.value = CreateProgressUiState.Error(
                                        errorMessageDescription = errorBody ?: "Unknown error"
                                    )
                                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                                }
                            } catch (e: Exception) {
                                _uiState.value = CreateProgressUiState.Error()
                            }
                        }
                        _uiState.value = CreateProgressUiState.Default(
                            familyMembers = responseData.familyMembers,
                            childNames = names,
                            childUserNames = usernames
                        )
                        _familyMembers.value = responseData.familyMembers
                        _childNames.value = names
                        _childUserNames.value = usernames
                    } else {
                        _uiState.value = CreateProgressUiState.Error()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = CreateProgressUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = CreateProgressUiState.Error()
            }
        }
    }
}