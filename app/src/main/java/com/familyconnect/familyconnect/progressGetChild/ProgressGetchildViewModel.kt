package com.familyconnect.familyconnect.progressGetChild


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val taskRepository: ProgressRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<List<Progress>>()
    val tasks: LiveData<List<Progress>> = _tasks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchTasks(userName: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = taskRepository.getProgressByUsername(userName)
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (!responseData.isNullOrEmpty()) {
                        _tasks.value = responseData
                    } else {
                        _errorMessage.value = "No tasks found"
                    }
                } else {
                    _errorMessage.value = "Error fetching tasks: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
