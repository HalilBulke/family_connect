package com.familyconnect.familyconnect.commoncomposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.task.Task
import com.familyconnect.familyconnect.util.DummyTasks
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskComponent(
    task: Task,
    onEdit: (Int) -> Unit,
    onComplete: (Int) -> Unit,
    onDelete: (Int) -> Unit = {},
    animDelay: Int = 100
) {

    val alphaAnimation = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(animDelay) {
        alphaAnimation.animateTo(targetValue = 1f, animationSpec = tween(1000, animDelay))
    }
    val priorityColors = listOf(
        Color.LightGray,
        Color.Yellow,
        Color.Red
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = alphaAnimation.value
            }
            .fillMaxWidth()
            .background(
                priorityColors[task.priority],
                RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .padding(start = 10.dp)
            .clickable {
                onEdit(task.id)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .padding(start = 8.dp, top = 10.dp, end = 8.dp, bottom = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { onComplete(task.id) },
                    modifier = Modifier
                        .size(32.dp)
                        .weight(0.1f)
                ) {
                    if (task.isCompleted) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Box(modifier = Modifier
                            .size(20.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onSecondary,
                                shape = CircleShape
                            ),
                            contentAlignment = Alignment.Center,
                            content = {})
                    }


                }
                Row(
                    modifier = Modifier.weight(0.8f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .basicMarquee(delayMillis = 1000),
                            text = task.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock),
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                text = task.getFormattedTime(),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }

                }
                if (task.date < LocalDate.now()) {
                    IconButton(
                        onClick = { onDelete(task.id) },
                        modifier = Modifier.weight(0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            tint = MaterialTheme.colorScheme.onSecondary,
                            contentDescription = null
                        )
                    }
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TaskComponentPreview() {
    val task = DummyTasks.dummyTasks[0]
    TaskComponent(
        task = task,
        {},
        {},
        {}
    )
}