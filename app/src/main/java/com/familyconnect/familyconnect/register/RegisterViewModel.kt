package com.familyconnect.familyconnect.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface RegisterUiState {
    object Default : RegisterUiState
    object FullNameNotValid : RegisterUiState
    object EmailNotValid : RegisterUiState
    object PasswordsNotMatch : RegisterUiState
    object Loading : RegisterUiState
    object RegisterError : RegisterUiState
    object Success : RegisterUiState

}
@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Default)
    val uiState: StateFlow<RegisterUiState> = _uiState

    private fun isValidFUllName(fullName: String): Boolean {
        return Regex("^[A-Za-zğüşıöçĞÜŞİÖÇ]+(['\\-\\s][A-Za-zğüşıöçĞÜŞİÖÇ]+)*\$").matches(fullName) && fullName.isNotEmpty()
    }
    private fun isValidEmail(email: String): Boolean {
        return Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matches(email) && email.isNotEmpty()
    }

    private fun isPasswordsMatch(password: String, passwordCheck: String): Boolean {
        return password == passwordCheck && password.isNotEmpty() && passwordCheck.isNotEmpty()
    }

    fun onRegisterClick(fullName: String, email: String, password: String, passwordCheck: String) {
        if (isValidFUllName(fullName).not()) {
            _uiState.value = RegisterUiState.FullNameNotValid
            return
        }
        if (isValidEmail(email).not()) {
            _uiState.value = RegisterUiState.EmailNotValid
            return
        }
        if (isPasswordsMatch(password, passwordCheck).not()) {
            _uiState.value = RegisterUiState.PasswordsNotMatch
            return
        }
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                /*

                if (response.isSuccessful) {


                    _uiState.value = RegisterUiState.Success

                } else {
                    _uiState.value = RegisterUiState.RegisterError
                }

                 */
            } catch (e: IOException) {
                _uiState.value = RegisterUiState.RegisterError
            }
        }
    }


}