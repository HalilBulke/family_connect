package com.familyconnect.familyconnect.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.familyconnect.familyconnect.task.Task
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
fun getTasksByMonth(tasks: List<Task>, month: YearMonth = YearMonth.now()): List<Task> {
    return tasks.filter { task ->
        YearMonth.from(task.date) == month
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun filterTasksByDate(tasks: List<Task>, date: LocalDate = LocalDate.now()): List<Task> {
    return tasks.filter { task ->
        task.date == date
    }
}