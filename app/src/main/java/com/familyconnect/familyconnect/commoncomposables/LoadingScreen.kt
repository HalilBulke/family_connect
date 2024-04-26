package com.familyconnect.familyconnect.commoncomposables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Suppress("MagicNumber", "LongMethod")
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val opacityValues = listOf(1f, 0.5f, 0.2f)
    val distance = with(LocalDensity.current) { 10.dp.toPx() }
    var index = 0

    Box(
        modifier = modifier
            .padding(bottom = 80.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(95.dp)
                .height(36.dp)
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0xFFCAE2E0),
                        1.0f to Color(0xFFACB3B2).copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(32.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(32.dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.6f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                circleValues.forEach { value ->
                    val circleColor = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFE65100).copy(alpha = opacityValues[index]),
                            Color(0xFFE65100)
                        ),
                        startX = 0f,
                        endX = 100f
                    )
                    index++
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .graphicsLayer {
                                translationY = -value * distance
                            }
                            .background(
                                brush = circleColor,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    LoadingScreen()
}
