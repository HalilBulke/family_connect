package com.familyconnect.familyconnect.task

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)

data class Task constructor(
    val id: Int = 0,
    val title: String = "",
    val isCompleted: Boolean = false,
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime = LocalTime.now(),
    val date: LocalDate = LocalDate.now(),
    val priority: Int = 0,
) {
    fun getWeekDaysTitle(): String {
        val weekDays = emptyList<Int>()
        val weekdaysTitle = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        val selectedDays = weekDays.map { weekdaysTitle[it] }
        return selectedDays.joinToString(", ")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedTime(): String {
        val dtf = DateTimeFormatter.ofPattern("hh:mm a")
        val startTimeFormat = startTime.format(dtf)
        val endTimeFormat = endTime.format(dtf)
        return "$startTimeFormat - $endTimeFormat"
    }

    @OptIn(ExperimentalStdlibApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDuration(checkPastTask: Boolean = false): Long {
        val startTimeSec = startTime.toSecondOfDay()
        val endTimeSec = endTime.toSecondOfDay()
        val currentTimeSec = LocalTime.now().toSecondOfDay()

        return when {
            checkPastTask -> {
                when {
                    currentTimeSec > endTimeSec -> 0
                    currentTimeSec in (startTimeSec + 1)..<endTimeSec -> (endTimeSec - currentTimeSec).toLong()
                    else -> (endTimeSec - startTimeSec).coerceAtLeast(0).toLong()
                }
            }

            else -> (endTimeSec - startTimeSec).coerceAtLeast(0).toLong()
        }
    }

    fun getDurationTimeStamp(duration: Long): String {
        val hours = duration / 3600
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60

        return if (hours == 0L) {
            String.format(
                "%02d:%02d",
                minutes,
                seconds
            )
        } else {
            String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedDuration(): String {
        val taskDuration = getDuration()

        val hours = (taskDuration / 3600).toInt()
        val minutes = ((taskDuration % 3600) / 60).toInt()

        val hoursString = if (hours == 1) "hour" else "hours"

        return when {
            hours > 0 && minutes > 0 -> String.format("%dh %02dm", hours, minutes)
            hours > 0 -> String.format("%d $hoursString", hours)
            else -> String.format("%d min", minutes)
        }

    }
}