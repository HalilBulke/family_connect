package com.familyconnect.familyconnect.addfamilymember

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFamilyMemberViewModel @Inject constructor(
    private val addFamilyMemberRepository: AddFamilyMemberRepository
) : ViewModel() {

    private val _addFamilyMemberResult = MutableLiveData<AddFamilyMemberResponse>()
    val addFamilyMemberResult: LiveData<AddFamilyMemberResponse> = _addFamilyMemberResult
    var addFamilyMemberRequest = AddFamilyMemberRequest(0, listOf())
    fun retry() {
        Log.d("retry", addFamilyMemberRequest.toString())
        addFamilyMember(addFamilyMemberRequest)
    }

    fun addFamilyMember(request: AddFamilyMemberRequest) {
        addFamilyMemberRequest = request
        viewModelScope.launch {
            try {
                val response = addFamilyMemberRepository.addFamilyMember(request)
                Log.d("MEMBER", response.body().toString())
                _addFamilyMemberResult.value = response.body()
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}
