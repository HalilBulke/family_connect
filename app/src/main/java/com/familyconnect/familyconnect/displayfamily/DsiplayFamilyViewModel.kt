package com.familyconnect.familyconnect.displayfamily

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFamilyViewModel @Inject constructor(
    private val familyRepository: DisplayFamilyRepository
) : ViewModel() {
    private val _familyData = MutableLiveData<Family>()
    val familyData: LiveData<Family> = _familyData

    // Additional LiveData fields for loading state and error message
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchFamily(userName: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = familyRepository.getFamily(userName)
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
                _isLoading.value = false
            }
        }
    }
}
