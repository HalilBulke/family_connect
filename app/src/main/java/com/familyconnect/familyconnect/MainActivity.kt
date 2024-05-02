package com.familyconnect.familyconnect

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberScreen
import com.familyconnect.familyconnect.allProgress.AllProgressScreen
import com.familyconnect.familyconnect.allProgress.AllProgressViewModel
import com.familyconnect.familyconnect.calendar.CalendarScreen
import com.familyconnect.familyconnect.calendar.CalendarViewModel
import com.familyconnect.familyconnect.childRewards.ChildRewardsScreen
import com.familyconnect.familyconnect.childRewards.ChildRewardsViewModel
import com.familyconnect.familyconnect.createevent.CreateEventScreen
import com.familyconnect.familyconnect.createevent.CreateEventViewModel
import com.familyconnect.familyconnect.createprogress.CreateProgressScreen
import com.familyconnect.familyconnect.displayfamily.MyFamilyScreen
import com.familyconnect.familyconnect.displayfamily.MyFamilyViewModel
import com.familyconnect.familyconnect.family.CreateFamilyScreen
import com.familyconnect.familyconnect.familyRewards.FamilyRewardsScreen
import com.familyconnect.familyconnect.familyRewards.FamilyRewardsViewModel
import com.familyconnect.familyconnect.login.LoginScreen
import com.familyconnect.familyconnect.maindashboard.MainDashboardView
import com.familyconnect.familyconnect.profile.ProfileScreen
import com.familyconnect.familyconnect.progressGetChild.GetProgressScreenchild
import com.familyconnect.familyconnect.progressGetChild.ProgressViewModel
import com.familyconnect.familyconnect.register.RegisterScreen
import com.familyconnect.familyconnect.showallgiventasks.AllTasksScreen
import com.familyconnect.familyconnect.showallgiventasks.AllTasksViewModel
import com.familyconnect.familyconnect.spin.SpinWheelScreen
import com.familyconnect.familyconnect.spin.SpinWheelViewModel
import com.familyconnect.familyconnect.task.CreateTaskScreen
import com.familyconnect.familyconnect.task.CreateTaskViewModel
import com.familyconnect.familyconnect.taskGetchild.ChildTasksScreen
import com.familyconnect.familyconnect.taskGetchild.ChildTasksViewModel
import com.familyconnect.familyconnect.ui.theme.FamilyConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            FamilyConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "login") {
                        composable(route = "login") {
                            LoginScreen(
                                onSuccess = {
                                    Log.d("familyID", it.familyId.toString())
                                    val familyId = it.familyId.toString()
                                    val name = it.name
                                    val role = it.authorities[0].roleId.toString()
                                    val username = it.username
                                    navController.navigate("dashboard/$familyId/$name/$role/$username")
                                },
                                onRegister = {
                                    navController.navigate("register")
                                },
                            )
                        }
                        composable(route = "register") {
                            RegisterScreen(
                                onSuccess = {
                                    navController.navigate("login")
                                }
                            )
                        }
                        composable(
                            route = "dashboard/{familyId}/{name}/{role}/{username}",
                            arguments = listOf(
                                navArgument(name = "familyId"){
                                    type = NavType.StringType
                                },
                                navArgument(name = "name"){
                                    type = NavType.StringType
                                },
                                navArgument(name = "role"){
                                    type = NavType.StringType
                                },
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }

                            )
                        )
                        {backstackEntry ->
                            //DashboardScreen()
                            MainDashboardView(navController = navController,
                                familyId = backstackEntry.arguments?.getString("familyId"),
                                name = backstackEntry.arguments?.getString("name"),
                                role = backstackEntry.arguments?.getString("role"),
                                username = backstackEntry.arguments?.getString("username"))
                        }

                        composable(
                            //route = "calendar?${KEY_USER_NAME}=${KEY_USER_NAME}",
                            route = "calendar/{username}/{familyId}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                },
                                navArgument(name = "familyId"){
                                    type = NavType.StringType
                                }
                            )
                        ){backstackEntry ->
                            val viewModel: CalendarViewModel = hiltViewModel()

                            CalendarScreen(
                                navController = navController,
                                userName = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                onReTryButtonClicked = { viewModel.retry() },
                                familyId = backstackEntry.arguments?.getString("familyId").orEmpty()
                            )
                        }

                        composable(route = "spinWheel/{username}",
                            arguments = listOf(
                                navArgument("username"){
                                    type = NavType.StringType
                                }
                            )
                        ){backstackEntry ->
                            val viewModel: SpinWheelViewModel = hiltViewModel()

                            SpinWheelScreen(
                                userName = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                viewModel = viewModel,
                                goRewardsScreen = {
                                    navController.navigate("childRewards/${backstackEntry.arguments?.getString("username")}")
                                }
                            )

                        }

                        composable(route = "createTask/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: CreateTaskViewModel = hiltViewModel()

                            CreateTaskScreen(
                                username = backstackEntry.arguments?.getString("username").orEmpty(),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                onReTryButtonClicked = { viewModel.retry() },
                            )
                        }
                        composable(route = "createProgress/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            CreateProgressScreen(
                                username = backstackEntry.arguments?.getString("username").orEmpty(),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                )
                        }
                        composable(route = "createFamily/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            CreateFamilyScreen(
                                username = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                onReTryButtonClicked = { navController.navigateUp() },
                                )
                        }

                        composable(route = "getTaskchild/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: ChildTasksViewModel = hiltViewModel()

                            ChildTasksScreen(
                                username = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                onAcceptButtonClicked = {username , taskId ->
                                    viewModel.acceptTask(username, taskId)
                                },
                                onRejectButtonClicked = {username , taskId ->
                                    viewModel.rejectTask(username, taskId)
                                },
                            )
                        }

                        composable(route = "profile/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            ProfileScreen(onLogout = {
                                navController.navigate("login")
                            },
                                username = backstackEntry.arguments?.getString("username"))
                        }

                        composable(route = "getProgresschild/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: ProgressViewModel = hiltViewModel()

                            GetProgressScreenchild(
                                username = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() },
                                onReTryButtonClicked = { viewModel.retry() },
                            )
                        }

                        composable(route = "displayFamily/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: MyFamilyViewModel = hiltViewModel()

                            MyFamilyScreen(
                                username = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                onReTryButtonClicked = { viewModel.retry() },
                            )
                        }


                        composable(
                            route = "addFamilyMember/{familyId}",
                            arguments = listOf(
                                navArgument(name = "familyId"){
                                    type = NavType.StringType
                                }
                            )) {backstackEntry ->
                            AddFamilyMemberScreen(
                                familyId = backstackEntry.arguments?.getString("familyId"),
                                onOkButtonClicked = { navController.navigateUp() },
                            )
                        }




                        //show all given tasks to parent
                        composable(route = "showallgiventasks/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: AllTasksViewModel = hiltViewModel()

                            AllTasksScreen(
                                userName = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() },
                                onAcceptButtonClicked = {username , taskId ->
                                    viewModel.acceptTask(username, taskId)
                                },
                                onRejectButtonClicked = {username , taskId ->
                                    viewModel.rejectTask(username, taskId)
                                },
                            )
                        }


                        //show all given progress to parent
                        composable(route = "showallgivenprogress/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: AllProgressViewModel = hiltViewModel()

                            AllProgressScreen(
                                username = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() },
                                onReTryButtonClicked = { viewModel.retry() },
                                onAcceptButtonClicked = {username , progressId ->
                                    viewModel.completeProgress(username, progressId)
                                },
                                onRejectButtonClicked = {username , progressId ->
                                    viewModel.cancelProgress(username, progressId)
                                },
                            )
                        }
                        composable(route = "createEvent/{username}/{familyId}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                },
                                navArgument(name = "familyId"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: CreateEventViewModel = hiltViewModel()

                            CreateEventScreen(
                                username = backstackEntry.arguments?.getString("username").orEmpty(),
                                familyId = backstackEntry.arguments?.getString("familyId").orEmpty(),
                                onOkButtonClicked = { navController.navigateUp() } ,
                                onReTryButtonClicked = { viewModel.retry() },
                            )
                        }

                        composable(route = "familyRewards/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: FamilyRewardsViewModel = hiltViewModel()

                            FamilyRewardsScreen(
                                username = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() },
                                onReTryButtonClicked = { viewModel.retry() },
                            )
                        }


                        composable(route = "childRewards/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            val viewModel: ChildRewardsViewModel = hiltViewModel()

                            ChildRewardsScreen(
                                username = backstackEntry.arguments?.getString("username"),
                                onOkButtonClicked = { navController.navigateUp() },
                                onReTryButtonClicked = { viewModel.retry() },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FamilyConnectTheme {
        Greeting("Android")
    }
}