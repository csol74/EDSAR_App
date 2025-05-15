package com.cesarsolano_ewdincanas.edsar_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import com.cesarsolano_ewdincanas.edsar_app.ui.theme.EDSAR_AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.navigation.NavType
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Subimos recetas al iniciar (opcional)
        RecetaUploader.subirRecetasDesdeJson(this)

        setContent {
            EDSAR_AppTheme {
                val navController = rememberNavController()
                var myStartDestination = "login"
                val auth = Firebase.auth
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    myStartDestination = "home"
                } else {
                    myStartDestination = "login"
                }

                NavHost(
                    navController = navController,
                    startDestination = myStartDestination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("login") {
                        LoginScreen(
                            onClickRegister = {
                                navController.navigate("register")
                            },
                            onSuccessfulLogin = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            onClickBack = {
                                navController.popBackStack()
                            },
                            onSuccessfulRegister = {
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        )
                    }

                    composable("home") {
                        HomeScreen(
                            onClickLogout = {
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            },
                            onClickProfile = {
                                navController.navigate("profile")
                            },
                            onClickNews = {
                                navController.navigate("news")
                            },
                            onClickRecetas = {
                                navController.navigate("listRecetas")
                            },
                            onNavigateToMeal = { mealType ->
                                // Navigate to the meals screen with the meal type as a parameter
                                navController.navigate("meals/$mealType")
                            }
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            onNavigateToHome = {
                                navController.navigate("home")
                            }
                        )
                    }

                    composable("news") {
                        News_Screen(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("listRecetas") {
                        ListRecetasScreen(
                            navController = navController,
                            onNavigateToHome = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = "meals/{mealType}",
                        arguments = listOf(navArgument("mealType") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val mealType = backStackEntry.arguments?.getString("mealType") ?: "Comida"
                        Meals_Screen(
                            mealType = mealType,
                            onBackToHome = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}