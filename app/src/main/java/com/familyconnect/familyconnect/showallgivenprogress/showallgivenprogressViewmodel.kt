package com.familyconnect.familyconnect.showallgivenprogress




import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import com.familyconnect.familyconnect.displayfamily.Family
import com.familyconnect.familyconnect.progressGetChild.Progress
import com.familyconnect.familyconnect.progressGetChild.ProgressApiService

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyFamilyViewModel3 @Inject constructor(
    private val familyRepository: DisplayFamilyRepository,
    private val taskApiService: ProgressApiService // Ensure this is injected properly
) : ViewModel() {
    private val _familyData = MutableLiveData<Family>()
    val familyData: LiveData<Family> = _familyData

    private val _tasksData = MutableLiveData<Map<String, List<Progress>>>()
    val tasksData: LiveData<Map<String, List<Progress>>> = _tasksData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchFamily(userName: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val familyResponse = familyRepository.getFamily(userName)
                if (familyResponse.isSuccessful && familyResponse.body() != null) {
                    val family = familyResponse.body()!!
                    _familyData.value = family
                    family.familyMembers.forEach { member ->
                        fetchTasksForMember(member)
                    }
                } else {
                    _errorMessage.value = "Failed to fetch family"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchTasksForMember(userName: String) {
        viewModelScope.launch {
            try {
                val tasksResponse = taskApiService.getProgressByUsername(userName)
                if (tasksResponse.isSuccessful && tasksResponse.body() != null) {
                    val tasks = tasksResponse.body()!!
                    _tasksData.value = _tasksData.value.orEmpty() + (userName to tasks)
                }
            } catch (e: Exception) {
                Log.e("VM", "Error fetching tasks for $userName: ${e.message}")
            }
        }
    }
}
