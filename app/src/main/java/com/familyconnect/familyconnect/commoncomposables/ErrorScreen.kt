package com.familyconnect.familyconnect.commoncomposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.familyconnect.familyconnect.util.DummyTasks

private const val BACKGROUND_ALPHA = 0.7f
private const val ERROR_IMG_SIZE = 44

@Composable
fun ErrorScreen(
    onClickFirstButton: () -> Unit,
    onClickSecondButton: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Beklenmedik bir hata ile karşılaşıldı",
    description: String = "Şu anda isteğinizi gerçekleştiremiyoruz",
    firstButtonText: String = "Tamam",
    secondButtonText: String = "Tekrar Dene"
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = BACKGROUND_ALPHA))
            .blur(8.dp)
    )
    Dialog(
        onDismissRequest = { }
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    tint = Color.Red,
                    contentDescription = null,
                    modifier = Modifier.size(ERROR_IMG_SIZE.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(36.dp))

                Button(
                    onClick = { onClickFirstButton() },
                    content = {
                        Text(
                            text = firstButtonText,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688), disabledContainerColor = Color.Gray),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onClickSecondButton() },
                    content = {
                        Text(
                            text = secondButtonText,
                            color = Color.Black,
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEB3B), disabledContainerColor = Color.Gray),
                )
            }
        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen({},{})
}