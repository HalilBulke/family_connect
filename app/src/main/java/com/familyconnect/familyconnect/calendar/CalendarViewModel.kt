package com.familyconnect.familyconnect.calendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberUiState
import com.familyconnect.familyconnect.task.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


sealed interface CalenderUiState {
    object Loading : CalenderUiState
    data class Error(val errorMessageTitle: String? = "Calendar Error",val errorMessageDescription: String? = "Error Description") :
        CalenderUiState
    data class Success(val calenderList: List<Task>?) : CalenderUiState
}

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calenderRepository: CalenderRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CalenderUiState>(CalenderUiState.Loading)
    val uiState: StateFlow<CalenderUiState> = _uiState

    private val userName: String
        get() = savedStateHandle.get<String>("username").orEmpty()

    init {
         Log.d("username", userName)
         loadTasks(userName = userName)
    }

    fun retry() {
        Log.d("retry", userName)
        loadTasks(userName = userName)
    }

    private fun loadTasks(userName: String) {
        viewModelScope.launch {
            try {
                val response = calenderRepository.getCalendar(userName)

                if (response.isSuccessful) {
                    _uiState.value = CalenderUiState.Success(
                        response.body()?.map {
                            Task(
                                id = it.id ?: 0,
                                title = "${it.name} ${it.description}",
                                isCompleted = it.status == "COMPLETED",
                                startTime = convertToLocalTime(it.startDate),
                                endTime = convertToLocalTime(it.dueDate),
                                priority = it.priority ?: 0,
                                date = convertToLocalDate(it.dueDate)
                            )
                        }
                    )
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = CalenderUiState.Error(
                        errorMessageDescription = errorBody ?: "Unknown error"
                    )
                    Log.d("ERROR BODY", errorBody ?: "Unknown error")
                }
            } catch (e: IOException) {
                _uiState.value = CalenderUiState.Error()
            }
        }
    }
    private fun convertToLocalDate(dateString: String?): LocalDate {
        // Tarih ve zaman bilgisi ile offset'i ayrıştırır.
        val odt = OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

        // Sadece tarih bilgisini alır.
        Log.d("DATENow",LocalDate.now().toString())
        Log.d("DATECreated",odt.toLocalDate().toString())
        return odt.toLocalDate()
    }

    private fun convertToLocalTime(timeString: String?): LocalTime {
        // ISO 8601 formatındaki string OffsetDateTime nesnesine dönüştürülür.
        if (timeString != null) {
            val odt = OffsetDateTime.parse(timeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            // Saat bilgisini LocalTime olarak alır.

            return odt.toLocalTime()
        }
        else {
            return LocalTime.now()
        }

    }
}