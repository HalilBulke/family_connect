package com.familyconnect.familyconnect.login

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import android.util.Log
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

    private var authority = Authority(
        roleId = 0,
        authority = "temp"
    )

    var user = User(
        userId = 1,
        username = "temp",
        password = "password",
        familyId = 123,
        name = "temp",
        authorities = listOf(authority),
        enabled = true,
        accountNonLocked = true,
        accountNonExpired = true,
        credentialsNonExpired = true
    )

    private fun isValidEmail(email: String): Boolean {
        return Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$").matches(email) && email.isNotEmpty()
    }
    private fun areInputsValid(email: String, password: String): Boolean {
        return isValidEmail(email) && password.isNotEmpty()
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
                    val responseBody = response.body()
                    Log.d("responseBody", responseBody.toString())
                    if (responseBody != null) {
                        if (responseBody.user != null){
                            val jwtToken = responseBody.jwt
                            user = responseBody.user
                            Log.d("user", user.toString())
                            if (jwtToken.isNotEmpty()){
                                _uiState.value = LoginUiState.Success
                                UserToken.saveToken("Bearer $jwtToken")
                                Log.d("UserToken", UserToken.getToken())
                            }
                            else {
                                _uiState.value = LoginUiState.LoginError
                            }
                        }
                        else {
                            _uiState.value = LoginUiState.LoginError
                        }

                    }
                } else {
                    _uiState.value = LoginUiState.LoginError
                }
            } catch (e: IOException) {
                _uiState.value = LoginUiState.LoginError
            }
        }
    }
}