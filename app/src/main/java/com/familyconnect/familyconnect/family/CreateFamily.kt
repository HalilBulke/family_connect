package com.familyconnect.familyconnect.family

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.AppInputField
import com.familyconnect.familyconnect.commoncomposables.ErrorScreen
import com.familyconnect.familyconnect.commoncomposables.ItemCard
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.util.makeToast

@Composable
fun CreateFamilyScreen(
    username: String?,
    viewModel: CreateFamilyViewModel = hiltViewModel(),
    onOkButtonClicked: () -> Unit,
    onReTryButtonClicked:() -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is CreateFamilyUiState.Error -> {
            ErrorScreen(
                onClickFirstButton = { onOkButtonClicked() },
                onClickSecondButton = { onReTryButtonClicked() },
            )
        }
        is CreateFamilyUiState.Loading -> {
            LoadingScreen()
        }
        is CreateFamilyUiState.Success -> {
            makeToast(LocalContext.current, "New family created Successfully")
        }
        is CreateFamilyUiState.Default -> {
            CreateFamilyPage(
                viewModel = viewModel,
                username = username,
                onOkButtonClicked = onOkButtonClicked,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFamilyPage(
    viewModel: CreateFamilyViewModel = hiltViewModel(),
    username : String?,
    onOkButtonClicked: () -> Unit,
) {
    var familyName by remember { mutableStateOf("") }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pageColor = Color(0xFFBB49CF)

    Column() {
        TopAppBar(
            title = { Text(text = "Create Family") },
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
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())){
            ItemCard (
                Modifier.padding(4.dp)
            ){
                Column (
                    modifier = Modifier.padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ){
                    Image(painter = painterResource(id = R.drawable.create_family), contentDescription = null)
                    Text(
                        text = "Create Family, Unlock Extraordinary Experiences Together!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF000000),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AppInputField(
                    value = familyName,
                    onValueChange = { familyName = it },
                    placeholderText = "Enter family name",
                    isResponseError = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        focusedBorderColor = pageColor,
                        unfocusedBorderColor = pageColor,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = "Create Family",
                    isLoading = false,
                    onClick = {
                        val family = CreateFamilyRequestBody(
                            familyName = familyName,
                            familyCreatorUserName = username.toString()
                        )
                        viewModel.createFamily(family)
                    },
                    buttonColor = pageColor,
                )
            }
        }


    }
}
