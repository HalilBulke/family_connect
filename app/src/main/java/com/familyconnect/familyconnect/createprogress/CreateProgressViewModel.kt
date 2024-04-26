package com.familyconnect.familyconnect.createprogress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProgressViewModel @Inject constructor(
    private val createProgressApiService: CreateProgressApiService
) : ViewModel() {

    fun addProgress(progress: CreateProgressRequestBody, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = createProgressApiService.addProgress(progress)
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