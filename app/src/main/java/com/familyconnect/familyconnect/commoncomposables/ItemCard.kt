package com.familyconnect.familyconnect.commoncomposables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    cardRadius: Dp = 8.dp,
    cardColor: Color = Color.White,
    cardElevation: Dp = 6.dp,
    content:
    @Composable
        () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(cardRadius),
        colors = CardDefaults.cardColors(cardColor),
        elevation = CardDefaults.cardElevation(cardElevation),
    )
    {
        content()
    }
}