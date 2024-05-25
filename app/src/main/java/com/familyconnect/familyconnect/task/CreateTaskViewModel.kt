package com.familyconnect.familyconnect.task

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import com.familyconnect.familyconnect.login.User
import com.familyconnect.familyconnect.maindashboard.MainDashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateTaskUiState {
    object Loading : CreateTaskUiState
    data class Error(val errorMessageTitle: String? = "Create Task Error",val errorMessageDescription: String? = "Error Description") : CreateTaskUiState
    data class final(val familyMembers: List<String>?, val childNames: List<String>?, val childUserNames: List<String>?) : CreateTaskUiState
    data class Success(val familyMembers: List<String>?, val childNames: List<String>?, val childUserNames: List<String>? ) : CreateTaskUiState
}

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val createTaskApiService: CreateTaskApiService,
    private val displayFamilyRepository: DisplayFamilyRepository,
    private val mainDashboardRepository: MainDashboardRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateTaskUiState>(CreateTaskUiState.Loading)
    val uiState: StateFlow<CreateTaskUiState> = _uiState

    private val _familyMembers = MutableStateFlow<List<String>>(listOf())

    private val _childNames = MutableStateFlow<List<String>>(emptyList())
    private val _childUserNames = MutableStateFlow<List<String>>(emptyList())

    private val _userData = MutableLiveData<User>()

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
        Log.d("username", userName)
        loadFamilyMembers(userName = userName)
    }

    fun retry() {
        Log.d("retry", userName)
        loadFamilyMembers(userName = userName)
    }

    fun addTask(task: CreateTaskRequestBody) {
        viewModelScope.launch {
            _uiState.value = CreateTaskUiState.Loading
            delay(500)
            try {
                val response = createTaskApiService.addTask(task)
                if (response.isSuccessful) {
                    _uiState.value = CreateTaskUiState.final(
                        familyMembers = _familyMembers.value,
                        childNames = _childNames.value,
                        childUserNames = _childUserNames.value)
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = CreateTaskUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = CreateTaskUiState.Error()
            }
        }
    }

    private fun loadFamilyMembers(userName: String) {
        viewModelScope.launch {
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
                                    _uiState.value = CreateTaskUiState.Error()
                                }
                            } catch (e: Exception) {
                                _uiState.value = CreateTaskUiState.Error()
                            }
                        }

                        _uiState.value = CreateTaskUiState.Success(
                            familyMembers = responseData.familyMembers,
                            childNames = names,
                            childUserNames = usernames
                        )
                        _familyMembers.value = responseData.familyMembers
                        _childNames.value = names
                        _childUserNames.value = usernames
                    } else {
                        _uiState.value = CreateTaskUiState.Error()
                    }
                } else {
                    _uiState.value = CreateTaskUiState.Error()
                }
            } catch (e: Exception) {
                _uiState.value = CreateTaskUiState.Error()
            }
        }
    }

}
