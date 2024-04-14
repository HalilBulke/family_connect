package com.familyconnect.familyconnect.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.familyconnect.familyconnect.spin.SpinWheelItem
import kotlin.random.Random

fun String.toColor(): Color {
    return if (this.length == 6) {
        val r = this.substring(0, 2).toInt(16)
        val g = this.substring(2, 4).toInt(16)
        val b = this.substring(4, 6).toInt(16)
        Color(r, g, b)
    } else {
        Color.White
    }
}

fun List<Color>.toBrush(endY: Float): Brush =
    if (this.size == 1) {
        Brush.verticalGradient(colors = this)
    } else {
        val colorStops = this.mapIndexed { index, color ->
            val stop = if (index == 0) 0f else (index.toFloat() + 1f) / this.size.toFloat()
            Pair(stop, color)
        }.toTypedArray()
        Brush.verticalGradient(
            colorStops = colorStops,
            endY = endY,
        )
    }

fun getDegreeFromSection(items: List<SpinWheelItem>, section: Int): Float {
    val pieDegree = 360f / items.size
    return pieDegree * section.times(-1)
}

fun getDegreeFromSectionWithRandom(items: List<SpinWheelItem>, section: Int): Float {
    val pieDegree = 360f / items.size
    val exactDegree = pieDegree * section.times(-1)

    val pieReduced = pieDegree * 0.9f //to avoid stop near border
    val multiplier = if (Random.nextBoolean()) 1f else -1f //before or after exact degree
    val randomDegrees = Random.nextDouble(0.0, pieReduced / 2.0)
    return exactDegree + (randomDegrees.toFloat() * multiplier)
}