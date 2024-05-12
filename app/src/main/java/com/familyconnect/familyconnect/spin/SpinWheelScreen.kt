package com.familyconnect.familyconnect.spin

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.EmptyTaskComponent
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.ItemCard
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.displayfamily.FamilySpinDataDTO
import com.familyconnect.familyconnect.displayfamily.SpinWheel
import com.familyconnect.familyconnect.util.toColor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpinWheelScreen(
    viewModel: SpinWheelViewModel = hiltViewModel(),
    userName : String?,
    onOkButtonClicked: () -> Unit,
    goRewardsScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is SpinWheelUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onOkButtonClicked() },
            )
        }
        is SpinWheelUiState.Loading -> {
            LoadingScreen()
        }
        is SpinWheelUiState.Success -> {
            SpinWheelPage(
                spinList = (uiState as SpinWheelUiState.Success).spinList,
                onOkButtonClicked = onOkButtonClicked,
                viewModel = viewModel,
                goRewardsScreen = goRewardsScreen,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SpinWheelPage(
    viewModel: SpinWheelViewModel,
    spinList: List<SpinWheel>?,
    onOkButtonClicked: () -> Unit,
    goRewardsScreen: () -> Unit,
) {
    var showSpin by remember { mutableStateOf(false) }
    var spinId by remember { mutableStateOf(0) }



    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pageColor = Color(0xFFF44336)

    Column {
        TopAppBar(
            title = { Text(text = "My Spin Wheels") },
            navigationIcon = {
                IconButton(onClick = { onOkButtonClicked() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = pageColor,
                actionIconContentColor = pageColor,
                navigationIconContentColor = Color.White,
                scrolledContainerColor = pageColor,
                titleContentColor = Color(0xFFFFFFFF),
            )
        )
        if (spinList.isNullOrEmpty()) {
            EmptyTaskComponent(
                text = "It seems you have no more spins left at the moment.",
                color = pageColor
            )
        } else if (!showSpin){
            ItemCard(
                Modifier.padding(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.spin_wheel_icon),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit,
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            )
            {
                itemsIndexed(items = spinList) { index, spin ->
                    Box(
                        modifier = Modifier.animateItemPlacement(tween(500))
                    ) {
                        ItemCard(modifier = Modifier.padding(4.dp)) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_gift),
                                    tint = Color.Unspecified,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(horizontal = 16.dp)
                                )
                                Column {
                                    Text(
                                        text = "You can win one of the prizes below by turning this wheel.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = pageColor,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        textAlign = TextAlign.Start
                                    )
                                    Text(
                                        text = spin.spinRewards.joinToString(separator = "\n") { "• $it" },
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(
                                        0xFF4CAF50
                                    )
                                    ),
                                    onClick = {
                                        showSpin = true
                                        spinId = spin.id
                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(text = "Spin the wheel")
                                }
                            }
                        }
                    }
                }
            }
        } else {
            val colors1 = remember {
                listOf(
                    "380048",
                    "2B003D",
                    "40004A",
                    "590058",
                    "730067"
                ).map { it.toColor() }
            }

            val colors2 = remember {
                listOf(
                    "F9A114",
                    "FD7D1B",
                    "F9901A",
                    "F6A019",
                    "EFC017"
                ).map { it.toColor() }
            }

            val colors3 = remember {
                listOf(
                    "003366",
                    "004477",
                    "005588",
                    "006699",
                    "0077AA"
                ).map { it.toColor() }
            }

            val colors4 = remember {
                listOf(
                    "990000",
                    "CC0000",
                    "FF3300",
                    "FF6600",
                    "FF9900"
                ).map { it.toColor() }
            }

            val colors5 = remember {
                listOf(
                    "009933",
                    "339933",
                    "669933",
                    "999933",
                    "CCCC33"
                ).map { it.toColor() }
            }

            val colors6 = remember {
                listOf(
                    "006699",
                    "0099CC",
                    "66CCCC",
                    "99CCCC",
                    "0099FF"
                ).map { it.toColor() }
            }

            val colors7 = remember {
                listOf(
                    "6600CC",
                    "9900CC",
                    "CC00CC",
                    "FF00FF",
                    "CC66CC"
                ).map { it.toColor() }
            }

            val colors8 = remember {
                listOf(
                    "330033",
                    "660033",
                    "990033",
                    "CC0033",
                    "FF0066"
                ).map { it.toColor() }
            }

            val colors9 = remember {
                listOf(
                    "003399",
                    "336699",
                    "669999",
                    "99CCCC",
                    "3399FF"
                ).map { it.toColor() }
            }

            val colors10 = remember {
                listOf(
                    "003300",
                    "003333",
                    "003366",
                    "003399",
                    "0033CC"
                ).map { it.toColor() }
            }

            val allColors = listOf(
                colors1, colors2, colors3, colors4, colors5,
                colors6, colors7, colors8, colors9, colors10
            )

            val selectedSpin = spinList.find { it.id == spinId }

            val items = remember {
                selectedSpin?.spinRewards?.mapIndexed { index, reward ->
                    val colors = allColors[index % allColors.size]
                    SpinWheelItem(
                        colors = colors.toPersistentList()
                    ) {
                        Text(
                            text = reward,
                            style = TextStyle(color = Color.White, fontSize = 20.sp)
                        )
                    }
                }?.toPersistentList() ?: emptyList<SpinWheelItem>().toPersistentList()
            }
            var randomNumber by remember { mutableStateOf(0) }
            randomNumber = Random.nextInt(items.size)

            WheelItem(
                items,
                spinId,
                pageColor,
                viewModel,
                goRewardsScreen,
                onOkButtonClicked,
                randomNumber,
                selectedSpin
            )
        }
    }
}

@Composable
private fun WheelItem(
    items: PersistentList<SpinWheelItem>,
    spinId: Int,
    pageColor: Color,
    viewModel: SpinWheelViewModel,
    goRewardsScreen: () -> Unit,
    onOkButtonClicked: () -> Unit,
    randomNumber: Int,
    selectedSpin: SpinWheel?
) {
    var showSpinButton by remember { mutableStateOf(true) }



    Log.d("items_size", items.size.toString())
    Log.d("random_number", randomNumber.toString())
    val spinState = rememberSpinWheelState(
        items = items,
        backgroundImage = R.drawable.spin_wheel1,
        centerImage = R.drawable.ic_family_connect,
        indicatorImage = R.drawable.spin_wheel_tick,
        onSpinningFinished = null,
    )
    Log.d("kazanılan ödül", items[randomNumber].toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(300.dp)) {
            SpinWheelComponent(spinState)
        }
        Spacer(modifier = Modifier.size(80.dp))

        if (showSpinButton) {
            AppButton(
                buttonText = "SPIN",
                isLoading = false,
                onClick = {
                    showSpinButton = false
                    spinState.stoppingWheel(randomNumber)
                },
                buttonColor = pageColor,
            )
        }
        AnimatedVisibility(
            visible = showSpinButton.not(),
            enter = fadeIn(
                animationSpec = tween(delayMillis = 8000, durationMillis = 1000),
                initialAlpha = 0f
            ),
        ) {
            ItemCard {
                Column(
                    modifier = Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Congratulations you have won: \n ${
                            selectedSpin?.spinRewards?.get(
                                randomNumber
                            )
                        }",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFFFF9800),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFF4CAF50
                                )
                            ),
                            onClick = {
                                if (selectedSpin != null) {
                                    viewModel.setReward(
                                        FamilySpinDataDTO(
                                            id = selectedSpin.id,
                                            username = selectedSpin.spinOwner,
                                            prize = selectedSpin.spinRewards[randomNumber],
                                        )
                                    )
                                }
                                goRewardsScreen()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "View Prize Page")
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFFF44336
                                )
                            ),
                            onClick = {
                                if (selectedSpin != null) {
                                    viewModel.setReward(
                                        FamilySpinDataDTO(
                                            id = selectedSpin.id,
                                            username = selectedSpin.spinOwner,
                                            prize = selectedSpin.spinRewards[randomNumber],
                                        )
                                    )
                                }
                                onOkButtonClicked()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Go back")
                        }
                    }
                }
            }
        }
    }
}
