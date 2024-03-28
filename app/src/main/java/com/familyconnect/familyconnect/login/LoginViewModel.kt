package com.familyconnect.familyconnect.login

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familyconnect.familyconnect.register.RegisterScreenPostItemBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface LoginUiState {
    object Default : LoginUiState
    data class InputsNotValid(val isEmailError: Boolean, val isPasswordError: Boolean) :
        LoginUiState
    object Loading : LoginUiState
    object LoginError : LoginUiState
    object Success : LoginUiState

}
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Default)
    val uiState: StateFlow<LoginUiState> = _uiState

    private fun isValidEmail(email: String): Boolean {
        return Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matches(email) && email.isNotEmpty()
    }
    private fun areInputsValid(email: String, password: String): Boolean {
        if (isValidEmail(email) && password.isNotEmpty()) {
            return true
        }
        return false
    }
    fun onLoginClick(email: String, password: String) {
        if (areInputsValid(email, password).not()) {
            _uiState.value = LoginUiState.InputsNotValid(
                isEmailError = !isValidEmail(email),
                isPasswordError = password.isEmpty()
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val credentials = "$email:$password"
                val encodedCredentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                val authorizationHeader = "Basic $encodedCredentials"
                UserToken.saveToken(authorizationHeader)

                val loginBody = LoginScreenPostItemBody(email, password)
                val response = loginRepository.login(loginBody)

                if (response.isSuccessful) {
                   // val token = response.headers().values("Authorization")[0]

                    //UserToken.saveToken(token)
                    //TODO check response body
                    _uiState.value = LoginUiState.Success

                } else {
                    _uiState.value = LoginUiState.LoginError
                }
            } catch (e: IOException) {
                _uiState.value = LoginUiState.LoginError
            }
        }
    }
}