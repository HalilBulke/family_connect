package com.familyconnect.familyconnect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.familyconnect.familyconnect.dashboard.DashboardScreen
import com.familyconnect.familyconnect.displayfamily.MyFamilyScreen
import com.familyconnect.familyconnect.family.CreateFamilyScreen
import com.familyconnect.familyconnect.login.LoginScreen
import com.familyconnect.familyconnect.maindashboard.MainDashboardView
import com.familyconnect.familyconnect.register.RegisterScreen
import com.familyconnect.familyconnect.spin.SpinWheelScreen
import com.familyconnect.familyconnect.task.CreateTaskScreen
import com.familyconnect.familyconnect.taskGetchild.GetTaskScreenchild
import com.familyconnect.familyconnect.ui.theme.FamilyConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

                        composable(route = "calendar") {
                            DashboardScreen()

                        }

                        composable(route = "spinWheel") {
                            SpinWheelScreen()

                        }

                        composable(route = "createTask") {
                            CreateTaskScreen()
                        }


                        composable(route = "createFamily/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            CreateFamilyScreen(username = backstackEntry.arguments?.getString("username"))

                        }

                        composable(route = "getTaskchild/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            GetTaskScreenchild(username = backstackEntry.arguments?.getString("username"))

                        }

                        composable(route = "displayFamily/{username}",
                            arguments = listOf(
                                navArgument(name = "username"){
                                    type = NavType.StringType
                                }
                            )
                        )
                        {backstackEntry ->
                            MyFamilyScreen(username = backstackEntry.arguments?.getString("username"))

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