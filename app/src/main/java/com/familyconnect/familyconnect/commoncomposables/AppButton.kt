package com.familyconnect.familyconnect.commoncomposables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.familyconnect.familyconnect.R

@Composable
fun AppButton(
    buttonText: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    loadingText: String = stringResource(id = R.string.button_default_loading_text),
    onClick: () -> Unit = {},
    buttonColor: Color = Color(0xFF015B63),
    buttonVariantColor: Color = Color(0xFF82DCD3),
    textColor: Color = Color.White,

    ) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor, disabledContainerColor = buttonVariantColor),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        enabled = !isLoading,
        contentPadding = PaddingValues(
            vertical = dimensionResource(id = R.dimen.spacing_m),
            horizontal = dimensionResource(id = R.dimen.spacing_xxl)
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (isLoading) {
                    loadingText
                } else {
                    buttonText
                },
                color = textColor
            )
            if (isLoading) {
                CircularProgressIndicator(
                    color = textColor,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.spacing_xl))
                )
            }
        }
    }
}