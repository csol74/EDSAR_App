package com.cesarsolano_ewdincanas.edsar_app

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth

@Composable
fun LoginScreen( onClickRegister: () -> Unit = {}, onSuccessfulLogin: () -> Unit = {}
) {
    val auth = Firebase.auth
    val activity = LocalView.current.context as Activity

    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFA500)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.TopCenter) {
                Box(
                    modifier = Modifier
                        .background(Color.Black, shape = RoundedCornerShape(20.dp))
                        .padding(top = 64.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(48.dp))

                        Text(
                            text = "¡Por favor Inicia sesión!",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputEmail,
                            onValueChange = { inputEmail = it },
                            placeholder = { Text("Correo Electrónico", color = Color.Gray) },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            isError = emailError.isNotEmpty(),
                            supportingText = {
                                if (emailError.isNotEmpty()) {
                                    Text(emailError, color = Color.Red)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = inputPassword,
                            onValueChange = { inputPassword = it },
                            placeholder = { Text("Contraseña", color = Color.Gray) },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            isError = passwordError.isNotEmpty(),
                            supportingText = {
                                if (passwordError.isNotEmpty()) {
                                    Text(passwordError, color = Color.Red)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (loginError.isNotEmpty()) {
                            Text(loginError, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
                        }

                        Button(
                            onClick = {
                                val isValidEmail = validateEmail(inputEmail).first
                                val isValidPassword = validatePassword(inputPassword).first

                                emailError = validateEmail(inputEmail).second
                                passwordError = validatePassword(inputPassword).second

                                if (isValidEmail && isValidPassword) {
                                    auth.signInWithEmailAndPassword(inputEmail, inputPassword)
                                        .addOnCompleteListener(activity) { task ->
                                            if (task.isSuccessful) {
                                                onSuccessfulLogin()
                                            } else {
                                                loginError = when (task.exception) {
                                                    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrecta"
                                                    is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                                                    else -> "Error al iniciar sesión. Intenta de nuevo"
                                                }
                                            }
                                        }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Entrar", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "¿No estás registrado?",
                            color = Color.White,
                            fontSize = 14.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                onClickRegister()
                            }
                        )
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.edsar_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .offset(y = (-40).dp)
                )
            }
        }
    }
}





