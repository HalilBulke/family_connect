package com.familyconnect.familyconnect.familyHub

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.ui.theme.PurpleGrey80
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FamilyHubScreen(
    username: String?,
    viewModel: FamilyHubViewModel = hiltViewModel(),
    onOkButtonClicked: () -> Unit,
    onReTryButtonClicked:() -> Unit,
    onSendChatClickListener: (Message) -> Unit,
    onSendSurveyClickListener: (Survey) -> Unit,
    onVoteSurveyClickListener: (Vote) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is FamilyHubUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
            )
        }
        is FamilyHubUiState.Loading -> {
            LoadingScreen()
        }
        is FamilyHubUiState.Success -> {
            ChatScreen(
                username = username,
                allMessages = (uiState as FamilyHubUiState.Success).allMessages,
                onSendChatClickListener = onSendChatClickListener,
                onSendSurveyClickListener = onSendSurveyClickListener,
                onVoteSurveyClickListener = onVoteSurveyClickListener,
            )
        }
    }
}

@Composable
fun ChatScreen(
    username: String?,
    allMessages: List<ChatBaseMessage>?,
    onSendChatClickListener: (Message) -> Unit,
    onSendSurveyClickListener: (Survey) -> Unit,
    onVoteSurveyClickListener: (Vote) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val listState = rememberLazyListState()
        if (allMessages != null) {
            LaunchedEffect(allMessages.size) {
                listState.animateScrollToItem(allMessages.size)
            }
        }
        if (allMessages.isNullOrEmpty()) {
            Text(
                modifier = Modifier.weight(1f, true).padding(horizontal = 16.dp, vertical = 32.dp),
                text = "Welcome to Family Hub! Let's start sharing our thoughts, ideas, and plans. Your voice matters here.",
                color = Color(0xFFB39DDB),
                fontSize = 24.sp,
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(allMessages) { item ->
                    ChatItem(username, item, onVoteSurveyClickListener)
                }
            }
        }
        ChatBox(
            username,
            onSendChatClickListener,
            onSendSurveyClickListener,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}
@Composable
fun CreateSurveyDialog(
    username: String?,
    onCreateSurvey: (survey: Survey) -> Unit,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    val options = remember { mutableStateListOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Create Survey") },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF009688)
                ),
                onClick = {
                    onCreateSurvey(
                        Survey(
                            sender = username.orEmpty(),
                            description = description,
                            survey = options.filter { it.isNotBlank() },
                            timestamp = System.currentTimeMillis().toString()
                        )
                    )
                    onDismiss()
                }
            ) {
                Text(text = "Create")
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE57373)
                ),
                onClick = { onDismiss() }
            ) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Description") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                options.forEachIndexed { index, option ->
                    OptionTextField(
                        value = option,
                        onValueChange = { newValue -> options[index] = newValue },
                        onDeleteOption = { options.removeAt(index) },
                        label = "Option ${index + 1}"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4)
                    ),
                    onClick = { options.add("") }) {
                    Text(text = "Add Option")
                }
            }
        }
    )
}

@Preview
@Composable
fun CreateSurveyPreview(){
    CreateSurveyDialog("",{}, {})
}


@Composable
fun OptionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onDeleteOption: () -> Unit,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onDeleteOption,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Option"
            )
        }
    }
}



@Composable
fun ChatItem(username: String?, message: ChatBaseMessage, onVoteSurveyClickListener: (Vote) -> Unit,) {
    val backgroundColor = remember {
        val userColor = getUserColor(username)
        if (message.senderUsername == username) {
            PurpleGrey80
        } else {
            userColor
        }
    }
    if (message.type == "survey" && message.description.isNotBlank()) {
        SurveyComponent(
            username = username,
            message = message,
            onVoteSurveyClickListener = onVoteSurveyClickListener,
            backgroundColor = backgroundColor,
            )
    } else {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)) {
            Column(
                modifier = Modifier
                    .align(if (message.senderUsername == username) Alignment.End else Alignment.Start)
                    .clip(
                        RoundedCornerShape(
                            topStart = 48f,
                            topEnd = 48f,
                            bottomStart = if (message.senderUsername == username) 48f else 0f,
                            bottomEnd = if (message.senderUsername == username) 0f else 48f
                        )
                    )
                    .background(backgroundColor)
                    .padding(16.dp),
                horizontalAlignment = if (message.senderUsername == username) Alignment.End else Alignment.Start
            ) {
                if (message.senderUsername != username) {
                    Text(text = message.senderName, modifier = Modifier.padding(bottom = 4.dp), fontSize = 12.sp)
                }
                Text(text = message.message, fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
                Text(
                    text = formatTime(message.timestamp),
                    fontSize = 10.sp,
                    textAlign = if (message.senderUsername == username) TextAlign.End else TextAlign.Start
                )
            }
        }
    }
}


@Composable
fun SurveyComponent(
    backgroundColor:Color,
    username: String?,
    message: ChatBaseMessage,
    onVoteSurveyClickListener: (Vote) -> Unit,
    ) {
    var selectedOptionIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(key1 = message.id) {
        if (selectedOptionIndex == -1) {
            val userVote = message.surveyVotes.indexOfFirst { it.voters.contains(username) }
            if (userVote != -1) {
                selectedOptionIndex = userVote
            }
        }
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)) {
        Column(
            modifier = Modifier
                .align(if (message.senderUsername == username) Alignment.End else Alignment.Start)
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (message.senderUsername == username) 48f else 0f,
                        bottomEnd = if (message.senderUsername == username) 0f else 48f
                    )
                )
                .background(backgroundColor)
                .padding(16.dp),
            horizontalAlignment = if (message.senderUsername == username) Alignment.End else Alignment.Start
        ) {
            if (message.senderUsername != username) {
                Text(
                    text = message.senderName,
                    fontSize = 14.sp,
                )
            }
            Text(
                text = message.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            message.surveyVotes.forEachIndexed { index, option ->
                OptionWithProgressBar(
                    username = username,
                    id = message.id,
                    option = option,
                    index = index,
                    selectedOptionIndex = selectedOptionIndex,
                    onVoteSurveyClickListener = onVoteSurveyClickListener,
                    onOptionSelected = { optionIndex->
                        selectedOptionIndex = optionIndex
                        }
                    )
            }
            Text(
                text = formatTime(message.timestamp),
                fontSize = 10.sp,
                textAlign = if (message.senderUsername == username) TextAlign.End else TextAlign.Start
            )
        }
    }
}

@Composable
fun OptionWithProgressBar(
    username: String?,
    id: Int,
    option: SurveyFrontDTO,
    index: Int,
    selectedOptionIndex: Int?,
    onVoteSurveyClickListener: (Vote) -> Unit,
    onOptionSelected: (index: Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        RadioButton(
            selected = selectedOptionIndex == index,
            onClick = {
                onOptionSelected(index)
                onVoteSurveyClickListener(
                    Vote(
                        surveyId = id,
                        username = username.orEmpty(),
                        option = index,
                    )
                )
            }
        )
        Column {
            Text(
                text = option.option,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = calculateProgress(option),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = Color(0xFF00BCD4),
                trackColor = Color(0xFFD8D9DF),
            )
        }
    }
}


@Composable
fun calculateProgress(option: SurveyFrontDTO): Float {
    val totalVotes = option.voters.size
    val votedCount = option.voters.count { it.isNotBlank() }
    return if (totalVotes > 0) {
        votedCount.toFloat() / totalVotes.toFloat()
    } else {
        0f
    }
}

@Preview
@Composable
fun SurveyPreview(){
    val m = ChatBaseMessage(
        1,"survey",
        "tokibokit@gmail.com",
        "halil",
        "",
        "Bugün ne yapalım",
        listOf(
            SurveyFrontDTO(
                "yemek", listOf()
            ),
            SurveyFrontDTO(
                "parti", listOf()
            ),
            SurveyFrontDTO(
                "çay", listOf()
            ),
            ),
        System.currentTimeMillis().toString()
    )
    SurveyComponent(PurpleGrey80,"",m, {})
}


private val userColors = mutableMapOf<String, Color>()
private fun getUserColor(username: String?): Color {
    if (username.isNullOrEmpty()) return Color.Gray
    if (userColors.containsKey(username)) {
        return userColors[username]!!
    }

    val randomColor = Color(
        red = (0..255).random() / 255f,
        green = (0..255).random() / 255f,
        blue = (0..255).random() / 255f,
        alpha = 1f
    )

    userColors[username] = randomColor
    return randomColor
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(
    username: String?,
    onSendChatClickListener: (Message) -> Unit,
    onSendSurveyClickListener: (Survey) -> Unit,
    modifier: Modifier
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    var isDialogOpen by remember { mutableStateOf(false) }

    if (isDialogOpen) {
        CreateSurveyDialog(
            username = username,
            onCreateSurvey = onSendSurveyClickListener,
            onDismiss = { isDialogOpen = false }
        )
    }

    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = chatBoxValue,
            onValueChange = { newText ->
                chatBoxValue = newText
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(text = "Type something")
            }
        )
        IconButton(
            onClick = {
                isDialogOpen = true
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(color = PurpleGrey80)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.List,
                contentDescription = "Send",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = {
                val msg = chatBoxValue.text
                if (msg.isBlank()) return@IconButton
                onSendChatClickListener(
                    Message(
                        message = chatBoxValue.text,
                        sender = username.orEmpty(),
                        timestamp = System.currentTimeMillis().toString()
                    )
                )
                chatBoxValue = TextFieldValue("")
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(color = PurpleGrey80)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun formatTime(timestamp: String): String {
    val timeInMillis = timestamp.toLong()

    // Zaman damgasını tarih nesnesine dönüştür
    val date = Date(timeInMillis)

    // Saat formatını ayarla
    val dateFormat = SimpleDateFormat("HH:mm")

    // Saati belirli bir zaman dilimine göre ayarla (isteğe bağlı)
    dateFormat.timeZone = TimeZone.getDefault()

    // Saati biçimlendir ve döndür
    return dateFormat.format(date)
}