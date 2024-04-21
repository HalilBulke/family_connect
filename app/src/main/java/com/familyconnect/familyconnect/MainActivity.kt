package com.familyconnect.familyconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.familyconnect.familyconnect.dashboard.DashboardScreen
import com.familyconnect.familyconnect.login.LoginScreen
import com.familyconnect.familyconnect.maindashboard.MainDashboardView
import com.familyconnect.familyconnect.register.RegisterScreen
import com.familyconnect.familyconnect.spin.SpinWheelScreen
import com.familyconnect.familyconnect.task.CreateTaskScreen
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
                                    navController.navigate("dashboard")
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
                        composable(route = "dashboard") {
                            //DashboardScreen()
                            MainDashboardView(navController = navController)
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