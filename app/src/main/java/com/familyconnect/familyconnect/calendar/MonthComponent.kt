package com.familyconnect.familyconnect.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familyconnect.familyconnect.task.Task
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthDayComponent(
    day: CalendarDay,
    selected: Boolean,
    tasks: List<Task>? = null, // Task listesi ekleniyor
    indicator: Boolean = true,
    isDatePicker: Boolean = false,
    onClick: (LocalDate) -> Unit = {}
) {
    val textColor = if (selected) {
        Color.Cyan
    } else {
        if (day.position == DayPosition.MonthDate)
            Color.Black
        else
            Color.Gray
    }

    val dayTextStyle = if (isDatePicker) {
        androidx.compose.ui.text.TextStyle(
            fontSize = 12.sp,
        )
    } else {
        androidx.compose.ui.text.TextStyle(
            fontSize = 16.sp,
        )
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Color.Blue else Color.Transparent)
            .border(
                if (day.date == LocalDate.now()) 2.dp else (-1).dp,
                Color.Blue,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick(day.date) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = dayTextStyle,
                color = textColor
            )
            if (tasks != null) {
                Row {
                    for (task in tasks) {
                        if (task.date == day.date) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(Color.Yellow)
                                    .padding(start = 2.dp)
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (indicator && day.position == DayPosition.MonthDate) Color.Blue  else Color.Transparent)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSecondary,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MonthDayComponentPreview() {
    MonthDayComponent(CalendarDay(LocalDate.now(), DayPosition.MonthDate), false)
}