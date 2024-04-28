package com.familyconnect.familyconnect.createprogress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import com.familyconnect.familyconnect.displayfamily.Family
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProgressViewModel @Inject constructor(
    private val createProgressApiService: CreateProgressApiService,
    private val displayFamilyRepository: DisplayFamilyRepository
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


    private val _familyData = MutableLiveData<Family>()
    val familyData: LiveData<Family> = _familyData

    // Additional LiveData fields for loading state and error message
    private val _isLoading2 = MutableLiveData<Boolean>()
    val isLoading2: LiveData<Boolean> = _isLoading2

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadFamilyMembers(userName: String) {
        _isLoading2.value = true
        viewModelScope.launch {
            try {
                val response = displayFamilyRepository.getFamily(userName)
                Log.d("FAM", response.toString())
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        val family = Family(
                            id = responseData.id,
                            familyName = responseData.familyName,
                            familyMembers = responseData.familyMembers,
                            creatorUserName = responseData.creatorUserName
                        )
                        _familyData.value = family
                    } else {
                        _errorMessage.value = "Empty response data"
                    }
                } else {
                    _errorMessage.value = "User does not belong to any family"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading2.value = false
            }
        }
    }
}