package com.familyconnect.familyconnect.commoncomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.familyconnect.familyconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    isResponseError: Boolean,
    modifier: Modifier = Modifier,
    isInputError: Boolean = false,
    isPassword: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        unfocusedTextColor = Color.Black,
        disabledBorderColor = Color.Gray,
        focusedBorderColor = if (isInputError || isResponseError) {
            Color.Red
        } else {
            Color(0xFF015B63)
        },
        unfocusedBorderColor = if (isInputError || isResponseError) {
            Color.Red
        } else {
            Color(0xFF015B63)
        },
    ),
) {
    val isError = isInputError || isResponseError

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = if (isInputError) Color.Red else Color.DarkGray,
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        trailingIcon = {
            if (isError) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_dialog_alert),
                    contentDescription = null,
                    modifier = Modifier.size(R.dimen.spacing_m.dp)
                )
            }
        }
    )
}