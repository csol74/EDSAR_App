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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EDSAR_AppTheme  {

                val myNavController= rememberNavController()
                var myStartDestination= "login"
                val auth = Firebase.auth
                val currentUser= auth.currentUser


                if(currentUser != null){
                    myStartDestination= "home"
                }else{
                    myStartDestination="login"
                }

                NavHost(

                    navController= myNavController,
                    startDestination= myStartDestination,
                    modifier= Modifier.fillMaxSize()
                ){
                    composable("login"){
                        LoginScreen(onClickRegister = {
                            myNavController.navigate("register")
                        }, onSuccessfulLogin = {
                            myNavController.navigate("home"){
                                popUpTo("login"){inclusive = true}
                            }
                        } )
                    }
                    composable( "register"){
                        RegisterScreen(onClickBack = {
                            myNavController.popBackStack()
                        }, onSuccessfulRegister = {
                            myNavController.navigate("home"){
                                popUpTo(0)
                            }
                        })
                    }
                    composable("home"){
                        HomeScreen(onClickLogout = {
                            myNavController.navigate("login"){
                                popUpTo(0)
                            }
                        })
                    }
                }


            }
        }
    }
}





    
