package com.familyconnect.familyconnect.familyRewards

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.EmptyTaskComponent
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.ItemCard
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.displayfamily.FamilySpinDataDTO

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FamilyRewardsScreen(
    username: String?,
    viewModel: FamilyRewardsViewModel = hiltViewModel(),
    onOkButtonClicked: () -> Unit,
    onReTryButtonClicked:() -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is FamilyRewardsUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
            )
        }
        is FamilyRewardsUiState.Loading -> {
            LoadingScreen()
        }
        is FamilyRewardsUiState.Success -> {
            FamilyRewardsPage(
                familyRewardsList = (uiState as FamilyRewardsUiState.Success).familyRewardsList,
                onOkButtonClicked = onOkButtonClicked,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FamilyRewardsPage(
    familyRewardsList: List<Reward>?,
    onOkButtonClicked: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pageColor = Color(0xFF04D7F1)

    Column {
        TopAppBar(
            title = { Text(text = "All Family Rewards") },
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
        ItemCard(
            Modifier.padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_all_rewards),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }
        if (familyRewardsList.isNullOrEmpty()) {
            EmptyTaskComponent(
                text = "There is currently no rewards to display",
                color = pageColor
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            )
            {
                itemsIndexed(items = familyRewardsList) { _, reward ->
                    Box(
                        modifier = Modifier.animateItemPlacement(tween(500))
                    ) {
                        ItemCard(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_rewards_list),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(100.dp).padding(horizontal = 16.dp)
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Winner of the reward: ${
                                            reward.rewardOwner.substringBefore(
                                                "@"
                                            )
                                        }", fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Reward: ${reward.reward}", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
