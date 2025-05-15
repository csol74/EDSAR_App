package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToHome: () -> Unit,
) {
    val auth = Firebase.auth
    val db = Firebase.firestore
    val userId = auth.currentUser?.uid ?: ""
    val userEmail = auth.currentUser?.email ?: ""

    // Estado para los datos del usuario
    var userData by remember { mutableStateOf<UserModel?>(null) }

    // Efecto para cargar datos del usuario al entrar a la pantalla
    LaunchedEffect(userId) {
        val docSnapshot = db.collection("users").document(userId).get().await()
        if (docSnapshot.exists()) {
            userData = docSnapshot.toObject(UserModel::class.java)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Perfil", color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFA500))
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.edsar_logo),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar los datos del usuario
                Text(
                    text = "Hola ${userData?.name ?: ""}",
                    fontSize = 22.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Data fields
                ProfileDataField(label = "Nombre", value = userData?.name ?: "")
                ProfileDataField(label = "Sexo", value = userData?.sex ?: "")
                ProfileDataField(label = "Peso", value = "${userData?.weight ?: ""} kg")
                ProfileDataField(label = "Altura", value = "${userData?.height ?: ""} m")
                ProfileDataField(label = "Edad", value = "${userData?.age ?: ""} años")
                ProfileDataField(label = "Email", value = userEmail)
            }
        }
    }
}

@Composable
fun ProfileDataField(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = value,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}