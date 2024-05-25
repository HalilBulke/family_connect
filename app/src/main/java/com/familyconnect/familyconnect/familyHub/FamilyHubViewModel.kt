package com.familyconnect.familyconnect.familyHub

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.calendar.CalenderUiState
import com.familyconnect.familyconnect.progressGetChild.Progress
import com.familyconnect.familyconnect.showallgiventasks.AllTasksUiState
import com.familyconnect.familyconnect.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface FamilyHubUiState {
    object Loading : FamilyHubUiState
    data class Error(val errorMessageTitle: String? = "Family Hub Error",val errorMessageDescription: String? = "Error Description") :
        FamilyHubUiState
    data class Success(val allMessages: List<ChatBaseMessage>?) : FamilyHubUiState
}


@HiltViewModel
class FamilyHubViewModel @Inject constructor(
    private val familyHubRepository: FamilyHubRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<FamilyHubUiState>(FamilyHubUiState.Loading)
    val uiState: StateFlow<FamilyHubUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
        Log.d("username", userName)
        loadAllMessages(userName = userName)
    }

    fun retry() {
        Log.d("retry", userName)
        loadAllMessages(userName = userName)
    }

    private fun loadAllMessages(userName: String) {
        viewModelScope.launch {
            try {
                while (true) {
                    val response = familyHubRepository.getMessages(userName)

                    if (response.isSuccessful) {
                        _uiState.value = FamilyHubUiState.Success(
                            response.body()
                        )
                    } else {
                        val errorBody = response.errorBody()?.string()
                        _uiState.value = FamilyHubUiState.Error(
                            errorMessageDescription = errorBody ?: "Unknown error"
                        )
                        Log.d("ERROR BODY", errorBody ?: "Unknown error")
                    }
                    delay(1000)
                }

            } catch (e: IOException) {
                _uiState.value = FamilyHubUiState.Error()
            }
        }
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            try {
                val sendMessage = familyHubRepository.sendMessage(message)
                if (sendMessage.isSuccessful) {
                    Log.d("sendMesaage",sendMessage.toString())
                    loadAllMessages(userName)
                } else {
                    val errorBody = sendMessage.errorBody()?.string()
                    _uiState.value = FamilyHubUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                    Log.d("sendMesaage Else",sendMessage.toString())
                }
            } catch (e: Exception) {
                Log.d("sendMesaage catch",e.toString())
                _uiState.value = FamilyHubUiState.Error()
            }
        }
    }

    fun sendSurvey(survey: Survey) {
        viewModelScope.launch {
            try {
                val surveyResponse = familyHubRepository.sendSurvey(survey)
                if (surveyResponse.isSuccessful) {
                    loadAllMessages(userName)
                } else {
                    val errorBody = surveyResponse.errorBody()?.string()
                    _uiState.value = FamilyHubUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                _uiState.value = FamilyHubUiState.Error()
            }
        }
    }

    fun voteSurvey(vote: Vote) {
        viewModelScope.launch {
            try {
                val voteSurvey = familyHubRepository.voteSurvey(vote)
                if (voteSurvey.isSuccessful) {
                    Log.d("voteSurvey",voteSurvey.toString())
                    loadAllMessages(userName)
                } else {
                    val errorBody = voteSurvey.errorBody()?.string()
                    _uiState.value = FamilyHubUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                    Log.d("voteSurvey Else",voteSurvey.toString())
                }
            } catch (e: Exception) {
                Log.d("voteSurvey catch",e.toString())
                _uiState.value = FamilyHubUiState.Error()
            }
        }
    }
}