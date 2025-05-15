package com.cesarsolano_ewdincanas.edsar_app
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onClickBack: () -> Unit = {}, onSuccessfulRegister: () -> Unit = {}) {
    val auth = Firebase.auth
    val db = Firebase.firestore
    val activity = LocalView.current.context as Activity

    // Estados de campos
    var name by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Estados de error
    var nameError by remember { mutableStateOf("") }
    var sexError by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf("") }
    var heightError by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var registerError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color(0xFFFFA500))
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                Image(
                    painter = painterResource(id = R.drawable.user_profile),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(1.dp))
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.Black, shape = RoundedCornerShape(30.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(30.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // NOMBRE
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Nombre completo") },
                                isError = nameError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (nameError.isNotEmpty()) Text(nameError, color = Color.Red)
                                }
                            )
                            // SEXO
                            OutlinedTextField(
                                value = sex,
                                onValueChange = { sex = it },
                                label = { Text("Sexo (hombre, mujer)") },
                                isError = sexError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (sexError.isNotEmpty()) Text(sexError, color = Color.Red)
                                }
                            )
                            // PESO
                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("Peso (kg)") },
                                isError = weightError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (weightError.isNotEmpty()) Text(weightError, color = Color.Red)
                                }
                            )
                            // ALTURA
                            OutlinedTextField(
                                value = height,
                                onValueChange = { height = it },
                                label = { Text("Altura (metros)") },
                                isError = heightError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (heightError.isNotEmpty()) Text(heightError, color = Color.Red)
                                }
                            )
                            // EDAD
                            OutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                label = { Text("Edad") },
                                isError = ageError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (ageError.isNotEmpty()) Text(ageError, color = Color.Red)
                                }
                            )
                            // CORREO
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Correo electrónico") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Email,
                                        contentDescription = "Correo",
                                        tint = Color(0xFFFF9900)
                                    )
                                },
                                isError = emailError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (emailError.isNotEmpty()) Text(emailError, color = Color.Red)
                                }
                            )
                            // CONTRASEÑA
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Contraseña") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = "Contraseña",
                                        tint = Color(0xFFFF9900)
                                    )
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                isError = passwordError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (passwordError.isNotEmpty()) Text(passwordError, color = Color.Red)
                                }
                            )
                            // CONFIRMAR CONTRASEÑA
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                label = { Text("Confirmar Contraseña") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = "Confirmar",
                                        tint = Color(0xFFFF9900)
                                    )
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                isError = confirmPasswordError.isNotEmpty(),
                                modifier = Modifier.fillMaxWidth(),
                                supportingText = {
                                    if (confirmPasswordError.isNotEmpty()) Text(confirmPasswordError, color = Color.Red)
                                }
                            )

                            // BOTÓN REGISTRAR
                            Button(
                                onClick = {
                                    val (isNameValid, nameMsg) = validateName(name)
                                    val (isSexValid, sexMsg) = validateSex(sex)
                                    val (isWeightValid, weightMsg) = validateWeight(weight)
                                    val (isHeightValid, heightMsg) = validateHeight(height)
                                    val (isAgeValid, ageMsg) = validateAge(age)
                                    val (isEmailValid, emailMsg) = validateEmail(email)
                                    val (isPasswordValid, passwordMsg) = validatePassword(password)
                                    val (isConfirmValid, confirmMsg) =
                                        validateConfirmPassword(password, confirmPassword)

                                    nameError = nameMsg
                                    sexError = sexMsg
                                    weightError = weightMsg
                                    heightError = heightMsg
                                    ageError = ageMsg
                                    emailError = emailMsg
                                    passwordError = passwordMsg
                                    confirmPasswordError = confirmMsg

                                    if (
                                        listOf(
                                            isNameValid, isSexValid, isWeightValid, isHeightValid,
                                            isAgeValid, isEmailValid, isPasswordValid, isConfirmValid
                                        ).all { it }
                                    ) {
                                        registerError = ""

                                        // Crear usuario en Authentication
                                        auth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(activity) { task ->
                                                if (task.isSuccessful) {
                                                    // Usuario creado con éxito, ahora guardar los datos en Firestore
                                                    val userId = auth.currentUser?.uid ?: ""

                                                    // Crear modelo de usuario
                                                    val user = UserModel(
                                                        userId = userId,
                                                        name = name,
                                                        sex = sex,
                                                        weight = weight,
                                                        height = height,
                                                        age = age,
                                                        email = email
                                                    )

                                                    // Guardar en Firestore
                                                    db.collection("users")
                                                        .document(userId)
                                                        .set(user)
                                                        .addOnSuccessListener {
                                                            onSuccessfulRegister()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            registerError = "Error al guardar datos: ${e.message}"
                                                        }
                                                } else {
                                                    registerError = task.exception?.message ?: "Error al registrar"
                                                }
                                            }
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Registrarme", color = Color.White, fontSize = 16.sp)
                            }

                            if (registerError.isNotEmpty()) {
                                Text(
                                    text = registerError,
                                    color = Color.Red,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}