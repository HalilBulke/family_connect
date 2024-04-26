package com.familyconnect.familyconnect.main

import com.familyconnect.familyconnect.task.Task

sealed class HomeScreenEvent {
    data class OnCompleted(val taskId: Int, val isCompleted: Boolean) : HomeScreenEvent()
    data class OnSwipeTask(val task: Task) : HomeScreenEvent()
    data class OnEditTask(val taskId: Int) : HomeScreenEvent()
    data class OnPomodoro(val taskId: Int) : HomeScreenEvent()
    data class OnDeleteTask(val taskId: Int) : HomeScreenEvent()
}