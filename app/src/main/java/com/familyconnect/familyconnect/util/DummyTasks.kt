package com.familyconnect.familyconnect.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.familyconnect.familyconnect.task.Task
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

object DummyTasks {

    @RequiresApi(Build.VERSION_CODES.O)
    val dummyTasks = generateDummyTasks()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateDummyTasks(count: Int = 5): List<Task> {
        val tasks = mutableListOf<Task>()

        for (i in 1..count) {
            val task = Task(
                id = i,
                title = "Task $i",
                isCompleted = Random.nextBoolean(),
                startTime = LocalTime.now(),
                endTime = LocalTime.now().plusHours(Random.nextLong(1, 4)),
                priority = Random.nextInt(3),
                date = LocalDate.now()
            )
            tasks.add(task)
        }
        return tasks
    }
}