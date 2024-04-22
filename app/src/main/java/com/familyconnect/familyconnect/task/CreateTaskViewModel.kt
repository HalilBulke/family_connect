package com.familyconnect.familyconnect.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val createTaskApiService: CreateTaskApiService
) : ViewModel() {

    fun addTask(task: CreateTaskRequestBody, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = createTaskApiService.addTask(task)
                if (response.isSuccessful && response.body()?.success == true) {
                    onSuccess()
                } else {
                    onError(response.message())
                }
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "An unknown error occurred")
            }
        }
    }
}


