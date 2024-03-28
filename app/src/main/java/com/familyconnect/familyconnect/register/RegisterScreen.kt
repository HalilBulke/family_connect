package com.familyconnect.familyconnect.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField


/*
class MockRegisterRepository : RegisterRepository {
    override suspend fun register(): Response<Unit> {
        // Mock register implementation
        // For preview purposes, you can return a successful response
        return Response.success(Unit)
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    // Create a mock RegisterRepository for preview
    val registerRepository = MockRegisterRepository()

    // Create a mock ViewModel for preview, passing the mock RegisterRepository
    val viewModel = RegisterViewModel(registerRepository)

    // Define mock action for onSuccess
    val onSuccess = {}

    // Call the RegisterScreen Composable with mock data
    RegisterScreen(viewModel = viewModel, onSuccess = onSuccess)
}


*/

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onSuccess: () -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordCheck by rememberSaveable { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    if (uiState is RegisterUiState.Success) {
        onSuccess()
    } else {
        Column(
            modifier = Modifier
                .background(color = Color(0xFFBED1D3))
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_family_connect),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .wrapContentSize()
                )
                Text(
                    text = stringResource(id = R.string.register_header),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
            }

            AppInputField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholderText =
                if (uiState == RegisterUiState.FullNameNotValid) {
                    stringResource(id = R.string.full_name_error)
                } else {
                    stringResource(id = R.string.full_name)
                },
                isInputError = uiState == RegisterUiState.FullNameNotValid,
                isResponseError = false,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
            )

            AppInputField(
                value = email,
                onValueChange = { email = it },
                placeholderText =
                if (uiState == RegisterUiState.EmailNotValid) {
                    stringResource(id = R.string.email_error)
                } else {
                    stringResource(id = R.string.email_hint)
                },
                isInputError = uiState == RegisterUiState.EmailNotValid,
                isResponseError = false,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
            )

            AppInputField(
                value = password,
                onValueChange = { password = it },
                placeholderText = stringResource(id = R.string.password_hint),
                isInputError = uiState == RegisterUiState.PasswordsNotMatch,
                isResponseError = false,
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
            )

            AppInputField(
                value = passwordCheck,
                onValueChange = { passwordCheck = it },
                placeholderText = stringResource(id = R.string.password_check_hint),
                isInputError = uiState == RegisterUiState.PasswordsNotMatch,
                isResponseError = false,
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
            )

            AppButton(
                buttonText = "Register",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5.2f),
                isLoading = uiState == RegisterUiState.Loading,
                onClick = {
                    viewModel.onRegisterClick(fullName, email, password, passwordCheck)
                }
            )
        }
    }
}