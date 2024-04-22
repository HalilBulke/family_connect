package com.familyconnect.familyconnect.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateFamilyViewModel @Inject constructor(
    private val familyRepository: NetworkFamilyRepository
) : ViewModel() {
    fun createFamily(family: CreateFamilyRequestBody, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = familyRepository.createFamily(family)
                if (response.isSuccessful && response.body() != null) {
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
