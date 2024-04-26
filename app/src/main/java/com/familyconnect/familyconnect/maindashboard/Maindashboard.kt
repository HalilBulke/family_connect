package com.familyconnect.familyconnect.maindashboard


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.familyconnect.familyconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardView(
    navController: NavHostController,
    familyId : String?,
    name : String?,
    role: String?,
    username: String?,
    viewModel: MainDashboardViewModel = hiltViewModel()
) {
//    Log.d("familyID", familyId.toString())
//    Log.d("NAME", name.toString())
//    Log.d("UserName", username.toString())
    viewModel.dashboardItems[3].route = "createFamily/$username"
    viewModel.dashboardItems[6].route = "getTaskchild/$username"
    viewModel.dashboardItems[8].route = "displayFamily/$username"
    viewModel.dashboardItems[10].route = "showallgiventasks/$username"
    viewModel.dashboardItems[12].route = "getProgresschild/$username"
    viewModel.dashboardItems[13].route = "showallgivenprogress/$username"
    // Fetch user data
    LaunchedEffect(key1 = username) {
        viewModel.fetchUserData(username.toString())
    }

    // Observe user data changes
    val userData by viewModel.userData.observeAsState()

    // Compute dashboard items based on user data
    val dashboardItems = remember(userData) {
        viewModel.filterDashboardItems(role.toString(), userData?.familyId != -1)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (dashboardItems.isEmpty()) {
                FamilyPhotoPlaceholder()
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "You need to be added to a family to use functions",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                FamilyPhotoPlaceholder()
                DashboardItemsGrid(
                    navController = navController,
                    items = dashboardItems
                )
            }
        }
    }
}



@Composable
fun DashboardItemsGrid(
    navController: NavHostController,
    items: List<DashboardItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        // Calculate the number of rows needed
        val numberOfRows = (items.size + 1) / 2
        items(numberOfRows) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (columnIndex in 0 until 2) {
                    val itemIndex = rowIndex * 2 + columnIndex
                    if (itemIndex < items.size) {
                        DashboardItemCard(
                            item = items[itemIndex],
                            navController = navController,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardItemCard(
    item: DashboardItem,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { navController.navigate(item.route) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.title,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    // Assuming BottomBarIcon is a previously defined composable that accepts an ImageVector and a String
    BottomBarIcon(Icons.Default.Home, "Home")
    BottomBarIcon(Icons.Default.Face, "Family")
    BottomBarIcon(Icons.Default.Settings, "Settings")
}

@Composable
fun BottomBarIcon(
    imageVector: ImageVector,
    contentDescription: String
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = Modifier.padding(16.dp)
    )
}


@Composable
fun FamilyPhotoPlaceholder() {
    // Assuming you have a drawable named 'ic_family_placeholder'
    Image(
        painter = painterResource(id = R.drawable.ic_family_connect),
        contentDescription = "Family photo placeholder",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Or whatever height you prefer
    )
}




// Note: Make sure to include the corresponding icons in your drawable resource folder and replace the placeholder IDs.
