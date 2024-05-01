package com.familyconnect.familyconnect.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.AppButton
import com.familyconnect.familyconnect.commoncomposables.LoadingScreen
import com.familyconnect.familyconnect.login.User
import com.familyconnect.familyconnect.util.makeToast

@Composable
fun ProfileScreen(onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    username: String?
) {
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is ProfileUiState.Error -> {

        }
        is ProfileUiState.Loading -> {
            LoadingScreen()
        }
        is ProfileUiState.Success -> {
            val user = (uiState as ProfileUiState.Success).userInfo
            Log.d("Profile", user.toString())
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text("User Profile", style = MaterialTheme.typography.headlineMedium)

                    Spacer(Modifier.weight(1f))
                    Text("Logout", style = MaterialTheme.typography.headlineMedium)
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Profile Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable(onClick = {
                                onLogout()
                            })
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                ProfilePage(user,viewModel)
            }

        }
    }
}

@Composable
fun ProfilePage(user: User, viewModel: ProfileViewModel) {

    val notification = rememberSaveable { mutableStateOf("") }
    var isLoadingName by remember { mutableStateOf(false) }


    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    var name by rememberSaveable { mutableStateOf(user.name) }
    var username by rememberSaveable { mutableStateOf(user.username) }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {

        ProfileImage()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email", modifier = Modifier.width(100.dp))
            TextField(
                readOnly = true,
                value = username,
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFE0FFFF),
                    textColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "User name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFE0FFFF),
                    textColor = Color.Black
                )
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val context = LocalContext.current
            AppButton(
                buttonText = "Update Name",
                isLoading = isLoadingName,
                onClick = {
                    isLoadingName = true;
                    viewModel.updateName(user.username, name, onSuccess = {
                        isLoadingName = false
                        makeToast(context, "Name updated as $name successfully")
                    }, onError = { error ->
                        makeToast(context, error)
                    })
                },
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        UpdatePasswordSection(user,viewModel)

    }
}

@Composable
fun ProfileImage() {
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_family_connect
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }
        Text(text = "Change profile picture")
    }
}

@Composable
fun UpdatePasswordSection(user: User, viewModel: ProfileViewModel) {
    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isLoadingPassword by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Old Password", modifier = Modifier.width(100.dp))
            TextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFE0FFFF),
                    textColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "New Password", modifier = Modifier.width(100.dp))
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFE0FFFF),
                    textColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Confirm Password", modifier = Modifier.width(100.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFE0FFFF),
                    textColor = Color.Black
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val context = LocalContext.current
            AppButton(
                buttonText = "Update Password",
                isLoading = isLoadingPassword,
                onClick = {
                    isLoadingPassword = true;
                    if (newPassword == confirmPassword) {
                        viewModel.updatePassword(user.username, oldPassword, newPassword, onSuccess = {
                            isLoadingPassword = false;
                            makeToast(context, "Password updated successfully")
                        }, onError = { error ->
                            isLoadingPassword = false;
                            makeToast(context, error)
                        })
                    } else {
                        isLoadingPassword = false;
                        makeToast(context, "Passwords do not match")
                    }
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

