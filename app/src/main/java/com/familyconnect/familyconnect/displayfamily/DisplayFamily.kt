package com.familyconnect.familyconnect.displayfamily

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.ItemCard
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen

@Composable
fun MyFamilyScreen(
    username: String?,
    viewModel: MyFamilyViewModel = hiltViewModel(),
    onOkButtonClicked: () -> Unit,
    onReTryButtonClicked:() -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is MyFamilyUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
            )
        }
        is MyFamilyUiState.Loading -> {
            LoadingScreen()
        }
        is MyFamilyUiState.Success -> {
            MyFamilyPage(
                family = (uiState as MyFamilyUiState.Success).family,
                onOkButtonClicked = onOkButtonClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFamilyPage(
    family: MyFamily,
    onOkButtonClicked: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pageColor = Color(0xFFA47CEC)
    val creator = family.creatorUserName
    Column {
        TopAppBar(
            title = { Text(text = "My Family") },
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
        ItemCard (
            Modifier.padding(4.dp)
        ){
            Column (
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(painter = painterResource(id = R.drawable.family_image), contentDescription = null)
                Text(
                    text = family.familyName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF2196F3),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Family Creator: $creator",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    textAlign = TextAlign.End
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
            itemsIndexed(items = family.familyMembers) { index, member ->
                ItemCard(modifier = Modifier.padding(4.dp)) {
                    Row {
                        Icon(
                            imageVector = if (member.role == "PARENT") Icons.Rounded.Person else Icons.Rounded.Face,
                            tint = Color(0xFF2196F3),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp).padding(horizontal = 16.dp)
                        )
                        Column {
                            Text(
                                text = member.role,
                                style = MaterialTheme.typography.bodyLarge,
                                color = pageColor,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = member.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = member.userName,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}
