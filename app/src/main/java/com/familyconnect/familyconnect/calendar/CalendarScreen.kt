package com.familyconnect.familyconnect.calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.commoncomposables.EmptyTaskComponent
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.commoncomposables.SnackbarController.showCustomSnackbar
import com.familyconnect.familyconnect.commoncomposables.TaskComponent
import com.familyconnect.familyconnect.main.HomeScreenEvent
import com.familyconnect.familyconnect.main.MainEvent
import com.familyconnect.familyconnect.task.Task
import com.familyconnect.familyconnect.util.DummyTasks
import com.familyconnect.familyconnect.util.filterTasksByDate
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalenderScreen(
    userName: String?,
    viewModel: CalendarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is CalenderUiState.Error -> {
            ErrorScreen(onClickFirstButton = { /*TODO*/ }, onClickSecondButton = { /*TODO*/ })
        }
        is CalenderUiState.Loading -> {
            LoadingScreen()
        }
        is CalenderUiState.Success -> {
            CalenderPage(
                tasks = (uiState as CalenderUiState.Success).calenderList.orEmpty(),
                onEvent = {},
                onMainEvent = {},
                onNavigate = {},
            ) {

            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalenderPage(
    tasks: List<Task>,
    onEvent: (HomeScreenEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onNavigate: (route: String) -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var calenderView by remember { mutableStateOf(CalenderView.MONTHLY) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    // monthly calender
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(50) }
    val endMonth = remember { currentMonth.plusMonths(50) }
    val monthState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    // weekly calender
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(100) }
    val endDate = remember { currentDate.plusDays(100) }
    val weekState = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstDayOfWeek = firstDayOfWeek
    )

    var selectedDay by remember { mutableStateOf<LocalDate>(currentDate) }
    var currentMonthTitle by remember { mutableStateOf(currentMonth.month) }
    currentMonthTitle = if (calenderView == CalenderView.WEEKLY)
        weekState.firstVisibleWeek.days[0].date.month
    else
        monthState.lastVisibleMonth.yearMonth.month

    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            title = {
                Text(
                    text = currentMonthTitle.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    ),
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    scope.launch {
                        selectedDay = currentDate
                        if (calenderView == CalenderView.WEEKLY)
                            weekState.animateScrollToWeek(currentDate)
                        else
                            monthState.animateScrollToMonth(currentMonth)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                }

                IconButton(onClick = {
                    calenderView = if (calenderView == CalenderView.WEEKLY)
                        CalenderView.MONTHLY
                    else
                        CalenderView.WEEKLY

                    onMainEvent(MainEvent.UpdateCalenderView(calenderView, context))
                }) {
                    val currentIcon =
                        if (calenderView == CalenderView.WEEKLY) Icons.Default.DateRange else Icons.Default.Menu
                    Icon(imageVector = currentIcon, contentDescription = null)
                }
            }
        )
    },
        floatingActionButton = {
            if (selectedDay >= LocalDate.now()) {
                FloatingActionButton(
                    onClick = {
                        onMainEvent(MainEvent.UpdateCalenderDate(selectedDay))
                        //onNavigate(Routes.AddTaskScreen.name)
                    },
                    containerColor = Color.Blue,
                    contentColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {

            AnimatedVisibility(visible = calenderView == CalenderView.WEEKLY) {
                WeekCalendar(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    state = weekState,
                    dayContent = { day ->
                        WeekDayComponent(
                            day,
                            selected = selectedDay == day.date,
                            indicator = filterTasksByDate(tasks, day.date).isNotEmpty()
                        ) {
                            selectedDay = it
                        }
                    },
                )
            }

            AnimatedVisibility(visible = calenderView == CalenderView.MONTHLY) {
                HorizontalCalendar(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    state = monthState,
                    dayContent = { day ->
                        MonthDayComponent(
                            day,
                            selected = selectedDay == day.date,
                            tasks = tasks,
                            indicator = filterTasksByDate(tasks, day.date).isNotEmpty()
                        ) {
                            selectedDay = it
                        }
                    },
                    monthHeader = { month ->
                        val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
                        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                    }
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 24.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.secondary
            )

            val selectedDayTasks = filterTasksByDate(tasks, selectedDay)
            if (selectedDayTasks.isEmpty()) {
                EmptyTaskComponent()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp, 0.dp)
                ) {
                    itemsIndexed(items = selectedDayTasks,
                        key = { index, task ->
                            task.id
                        }) { index, task ->
                        Box(
                            modifier = Modifier.animateItemPlacement(tween(500))
                        ) {
                            TaskComponent(
                                task = task,
                                onEdit = { taskId ->
                                    if (task.date >= LocalDate.now()) {
                                        onEvent(HomeScreenEvent.OnEditTask(taskId))
                                        //onNavigate("${Routes.EditTaskScreen.name}/$taskId")
                                    }
                                },
                                onComplete = {
                                    if (task.date >= LocalDate.now()) {
                                        onEvent(HomeScreenEvent.OnCompleted(it, !task.isCompleted))
                                    }
                                },
                                onDelete = {
                                    onEvent(HomeScreenEvent.OnDeleteTask(it))
                                    showCustomSnackbar(
                                        msg = "Task Deleted",
                                        actionText = "Undo",
                                        onClickAction = {}
                                    )
                                },
                                animDelay = index * 100
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CalenderScreenPreview() {
    val tasks = DummyTasks.dummyTasks
    CalenderPage(tasks, {}, {}, {}, {})
}