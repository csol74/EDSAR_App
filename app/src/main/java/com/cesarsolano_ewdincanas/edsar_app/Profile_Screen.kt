package com.cesarsolano_ewdincanas.edsar_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(

    onNavigateToHome: () -> Unit,
) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFA500))
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hola User1",
                fontSize = 22.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Crear los campos de datos del usuario
            val fields = listOf(
                "Nombre",
                "Sexo",
                "Peso",
                "Altura",
                "Edad:",
                "Email"
            )

            fields.forEach { texto ->
                OutlinedTextField(
                    value = texto,
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(0.9f),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Gray,
                        disabledContainerColor = Color.White,
                        disabledBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}