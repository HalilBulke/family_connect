package com.familyconnect.familyconnect.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    onRegister: () -> Unit,
) {
    var isLoginPageVisible by remember { mutableStateOf(false) }

    LaunchScreen(onComplete = { isLoginPageVisible = true })

    if (isLoginPageVisible) {
        LoginPage(
            viewModel = viewModel,
            onSuccess = onSuccess,
            onRegister = onRegister
        )
    }
}

@Composable
fun LaunchScreen(onComplete: () -> Unit) {
    Box(
        modifier = Modifier
            .background(color = Color.Blue)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash) ,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Center,
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
                text = "Famil Connect",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                modifier = Modifier.wrapContentSize()
            )
        }

    }
    LaunchedEffect(true) {
        delay(4.seconds)
        onComplete()
    }
}

@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    onSuccess: () -> Unit,
    onRegister: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState is LoginUiState.Success) {
        onSuccess()
    } else {
        LoginScreen(uiState = uiState, viewModel = viewModel, onRegister = onRegister)
    }
}

@Composable
fun LoginScreen(uiState: LoginUiState, viewModel: LoginViewModel, onRegister: () -> Unit) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        launch {
            isVisible = true
        }
    }

    Image(
        painter = painterResource(id = R.drawable.splash) ,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            initialAlpha = 0f,
            animationSpec = tween(durationMillis = 2000)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_family_connect),
                contentDescription = null,
                modifier = Modifier
                    .size(180.dp),
                contentScale = ContentScale.Crop
            )
            LoginBox(
                uiState = uiState,
                viewModel = viewModel,
                onRegister = onRegister,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
@Composable
fun LoginBox(uiState: LoginUiState, viewModel: LoginViewModel, onRegister: () -> Unit, modifier: Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val isEmailError = uiState is LoginUiState.InputsNotValid && uiState.isEmailError
    val isPasswordError = uiState is LoginUiState.InputsNotValid && uiState.isPasswordError
    val isLoginError = uiState is LoginUiState.LoginError
    val isLoading = uiState is LoginUiState.Loading

    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize(),
            shape = RoundedCornerShape(36.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(color = Color(0xFFBED1D3))
                    .wrapContentSize()
                    .padding(dimensionResource(id = R.dimen.spacing_xxl))
            ) {
                Text(
                    text = stringResource(id = R.string.login_header),
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                )
                if (isLoginError) {
                    Text(
                        text = stringResource(id = R.string.error_check_credentials),
                        color = Color.Red,
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_xxs)),
                    )
                }
                Spacer(modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.spacing_l)))
                if (isEmailError) {
                    Text(
                        text = stringResource(id = R.string.email_error),
                        color = Color.Red,
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_xxs)),
                    )
                }
                AppInputField(
                    isInputError = isEmailError,
                    isResponseError = isLoginError,
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier,
                    placeholderText = stringResource(id = R.string.email_hint),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    )
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_s)))
                AppInputField(
                    isInputError = isPasswordError,
                    isResponseError = isLoginError,
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier,
                    placeholderText =
                    if (isPasswordError) {
                        stringResource(id = R.string.password_hint)
                    } else {
                        stringResource(id = R.string.password_hint)
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.onLoginClick(email, password)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    isPassword = true,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_xl)))

                AppButton(
                    buttonText = stringResource(id = R.string.login_button_text),
                    loadingText = stringResource(id = R.string.loading_text),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .aspectRatio(5.2f),
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onLoginClick(email, password)
                    },
                    isLoading = isLoading,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.signup_header),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = stringResource(id = R.string.signup_button_text),
                        color = Color(0xFF015B63),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable { onRegister() }
                    )
                }
            }
        }
    }
}