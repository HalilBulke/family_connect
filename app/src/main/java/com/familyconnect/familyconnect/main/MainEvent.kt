package com.familyconnect.familyconnect.main

import android.content.Context
import com.familyconnect.familyconnect.calendar.CalenderView
import com.familyconnect.familyconnect.task.SortTask
import java.time.LocalDate
import java.time.LocalTime

sealed class MainEvent {
    data class UpdateTimePicker(val isWheelTimePicker: Boolean, val context: Context) : MainEvent()
    data class UpdateTimeFormat(val isTimeFormat: Boolean, val context: Context) : MainEvent()
    data class UpdateSleepTime(val sleepTime: LocalTime, val context: Context) : MainEvent()
    data class UpdateLanguage(val language: String, val context: Context) : MainEvent()
    data class UpdateSortByTask(val sortTask: SortTask, val context: Context) : MainEvent()
    data class UpdateCalenderView(val calenderView: CalenderView, val context: Context) :
        MainEvent()

    data class UpdateCalenderDate(val date: LocalDate?) : MainEvent()
}