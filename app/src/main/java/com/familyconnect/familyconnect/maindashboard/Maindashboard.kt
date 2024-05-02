package com.familyconnect.familyconnect.maindashboard


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.familyconnect.familyconnect.R
import com.familyconnect.familyconnect.commoncomposables.ItemCard

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

    // Fetch user data
    LaunchedEffect(key1 = username) {
        viewModel.fetchUserData(username.toString())
    }

    // Observe user data changes
    val userData by viewModel.userData.observeAsState()

    var famID = familyId

    if (userData?.familyId != null) {
        famID = userData?.familyId.toString()
    }


    viewModel.dashboardItems[0].route = "calendar/$username"
    viewModel.dashboardItems[1].route = "createTask/$username"
    viewModel.dashboardItems[2].route = "createFamily/$username"
    viewModel.dashboardItems[3].route = "spinWheel/$username"
    viewModel.dashboardItems[4].route = "getTaskchild/$username"
    viewModel.dashboardItems[5].route = "displayFamily/$username"
    viewModel.dashboardItems[6].route = "addFamilyMember/$famID"
    viewModel.dashboardItems[7].route = "showallgiventasks/$username"
    viewModel.dashboardItems[8].route = "createProgress/$username"
    viewModel.dashboardItems[9].route = "getProgresschild/$username"
    viewModel.dashboardItems[10].route = "showallgivenprogress/$username"
    viewModel.dashboardItems[11].route = "createEvent/$username/$famID"
    viewModel.dashboardItems[12].route = "familyRewards/$username"
    viewModel.dashboardItems[13].route = "childRewards/$username"
    viewModel.profileDashboardItem.route = "profile/$username"


    // Compute dashboard items based on user data
    val dashboardItems = remember(userData) {
        viewModel.filterDashboardItems(role.toString(), userData?.familyId != -1)
    }

    Scaffold(
        //bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {
            WelcomeMessage(userData?.name.toString(), viewModel.profileDashboardItem){
                navController.navigate(viewModel.profileDashboardItem.route)
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_s)))
            if (dashboardItems.isEmpty()) {
                FamilyPhoto()
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
                FamilyPhoto()
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
        colors = CardDefaults.cardColors(
            containerColor = item.color,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = item.title,
                modifier = Modifier.size(96.dp)
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun FamilyPhoto(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth().height(200.dp).padding(8.dp)
    ) {
        item {
            ItemCard {
                Image(
                    painter = painterResource(id = R.drawable.add_family_member),
                    contentDescription = "Family photo 1",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        item {
            ItemCard {
                Image(
                    painter = painterResource(id = R.drawable.create_family),
                    contentDescription = "Family photo 1",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        item {
            ItemCard {
                Image(
                    painter = painterResource(id = R.drawable.family111),
                    contentDescription = "Family photo 1",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun WelcomeMessage(name: String, item: DashboardItem, onProfileClick: () -> Unit) {
    ItemCard {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_family_connect),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable(onClick = {})
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "WELCOME $name!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable(onClick = onProfileClick)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}






